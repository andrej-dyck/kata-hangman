package ad.kata.hangman

import ad.kata.hangman.oo.ComputerHost
import ad.kata.hangman.oo.Guess
import ad.kata.hangman.oo.Guesses
import ad.kata.hangman.oo.MaxMisses
import ad.kata.hangman.oo.VerboseHost
import ad.kata.hangman.oo.Word
import ad.kata.hangman.oo.toSecret
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import ad.kata.hangman.procedural.Hangman as ProceduralHangman

class RefactoringRegressionTests {

    @ParameterizedTest
    @CsvSource(
        "experiment, exv",
        "hangman, x",
        "hangman, ax",
        "a, x",
        "a, a",
        "book, o",
        "book, x",
        "book, ob",
        "book, xob",
        "book, kob",
        "elegant, e",
        "objects, x",
        "objects, st"
    )
    fun `secret word obscures hidden letters with ?-marks and reveals all guessed letters`(
        word: Word,
        guesses: String
    ) {
        assertThat(
            word.toSecret()
                .reveal(guesses.toCharArray())
                .toString()
        ).isEqualTo(
            proceduralExec(word, guesses)
                .last { it.startsWith("The word: ") }
                .removePrefix("The word: ")
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["hangman", "book", "elegant", "objects"])
    fun `host reveals secret word after guesses`(word: Word) {
        val guesses = word.toMinimalGuesses()

        assertThat(
            wordReveals(ooExec(word, guesses))
                .drop(1) // procedural version does not show first all-? word
        ).containsExactlyElementsOf(
            wordReveals(proceduralExec(word, guesses))
        )
    }

    private fun wordReveals(outputLines: List<String>) =
        outputLines
            .filter { it.startsWith("The word: ") }
            .map { it.removePrefix("The word: ") }

    @ParameterizedTest
    @ValueSource(strings = ["hangman", "book", "elegant", "objects"])
    fun `gives feedback on hit or miss after each guess`(word: Word) {
        val guesses = word.toMinimalGuesses()

        assertThat(
            hitsAndMisses(ooExec(word, guesses))
        ).containsExactlyElementsOf(
            hitsAndMisses(proceduralExec(word, guesses))
        )
    }

    private fun hitsAndMisses(outputLines: List<String>) =
        outputLines
            .filter { it.contains("Hit") || it.contains("Missed") }
            .map { if (it.contains("Hit")) "Hit" else "Missed" }

    @ParameterizedTest
    @CsvSource(
        "hangman, 1, anx",
        "book, 5, eokb",
        "elegant, 3, engitkm",
        "objects, 5, xxxxx"
    )
    fun `plays hangman until word is revealed or attempts are exhausted`(word: Word, maxMisses: Int, guesses: String) {
        assertThat(
            wordReveals(ooExec(word, guesses, maxMisses))
                .drop(1) // procedural version does not show first all-? word
        ).containsExactlyElementsOf(
            wordReveals(proceduralExec(word, guesses, maxMisses))
        )
    }
}

private fun proceduralExec(word: Word, guesses: Guesses) =
    proceduralExec(word, guesses.map { it.letter })

private fun proceduralExec(word: Word, guesses: Sequence<Char>) =
    proceduralExec(word, guesses.toList().toCharArray())

private fun proceduralExec(word: Word, guesses: String) =
    proceduralExec(word, guesses.toCharArray())

private fun proceduralExec(word: Word, guesses: String, maxMisses: Int) =
    proceduralExec(word, guesses.toCharArray(), maxMisses)

private fun proceduralExec(word: Word, inputChars: CharArray, maxMisses: Int = Int.MAX_VALUE) =
    ByteArrayOutputStream().use { output ->
        ProceduralHangman(
            ByteArrayInputStream(
                inputChars.toLines(trailingBreak = true).toByteArray()
            ),
            output,
            arrayOf(word.toString()),
            maxMisses
        ).exec()

        output.toString()
    }.nonEmptyLines()

private fun ooExec(word: Word, guesses: String, maxMisses: Int) =
    ooExec(word, Guesses(guesses.toCharArray().asSequence().map { Guess(it) }), MaxMisses(maxMisses))

private fun ooExec(word: Word, guesses: Guesses, maxMisses: MaxMisses = IGNORE_MISSES) =
    ByteArrayOutputStream().use { output ->
        VerboseHost(ComputerHost(word), output)
            .take(guesses, maxMisses)
            .toList()

        output.toString()
    }.nonEmptyLines()