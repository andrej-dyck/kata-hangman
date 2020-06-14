package ad.kata.hangman

import ad.kata.hangman.oo.*
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

        fun wordReveals(outputLines: List<String>) =
            outputLines
                .filter { it.startsWith("The word: ") }
                .map { it.removePrefix("The word: ") }

        assertThat(
            wordReveals(ooExec(word, guesses))
                .drop(1) // procedural version does not show first all-? word
        ).containsExactlyElementsOf(
            wordReveals(proceduralExec(word, guesses))
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["hangman", "book", "elegant", "objects"])
    fun `gives feedback on hit or miss after each guess`(word: Word) {
        val guesses = word.toMinimalGuesses()

        fun hitsAndMisses(outputLines: List<String>) =
            outputLines
                .filter { it.contains("Hit") || it.contains("Missed") }
                .map { if (it.contains("Hit")) "Hit" else "Missed" }

        assertThat(
            hitsAndMisses(ooExec(word, guesses))
        ).containsExactlyElementsOf(
            hitsAndMisses(proceduralExec(word, guesses))
        )
    }
}

private fun proceduralExec(word: Word, guesses: Guesses) =
    proceduralExec(word, guesses.toList().toCharArray())

private fun proceduralExec(word: Word, guesses: String) =
    proceduralExec(word, guesses.toCharArray())

private fun proceduralExec(word: Word, inputChars: CharArray, maxMistakes: Int = Int.MAX_VALUE) =
    ByteArrayOutputStream().use { output ->
        ProceduralHangman(
            ByteArrayInputStream(
                inputChars.toLines(trailingBreak = true).toByteArray()
            ),
            output,
            arrayOf(word.toString()),
            maxMistakes
        ).exec()

        output.toString()
    }.nonEmptyLines()

private fun ooExec(word: Word, guesses: Guesses, maxMistakes: Int = Int.MAX_VALUE) =
    ByteArrayOutputStream().use { output ->
        VerboseHost(ComputerHost(word), output)
            .take(guesses)
            .toList()

        output.toString()
    }.nonEmptyLines()