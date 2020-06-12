package ad.kata.hangman

import ad.kata.hangman.oo.ComputerHost
import ad.kata.hangman.oo.Guesses
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
            proceduralExec(word.toString(), guesses)
                .lines()
                .last { it.startsWith("The word: ") }
                .removePrefix("The word: ")
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["hangman", "book", "elegant", "objects"])
    fun `host reveals secret word after guesses`(word: Word) {
        val guesses = word.toMinimalGuesses()

        assertThat(
            ComputerHost(word)
                .take(guesses)
                .map { it.toString() }
                .toList()
        ).containsExactlyElementsOf(
            proceduralExec(word.toString(), guesses)
                .lines()
                .filter { it.startsWith("The word: ") }
                .map { it.removePrefix("The word: ") }
        )
    }
}

private fun proceduralExec(word: String, guesses: Guesses) =
    proceduralExec(word, Int.MAX_VALUE, *guesses.toList().toCharArray())

private fun proceduralExec(word: String, guesses: String) =
    proceduralExec(word, Int.MAX_VALUE, *guesses.toCharArray())

private fun proceduralExec(word: String, vararg inputChars: Char) =
    proceduralExec(word, Int.MAX_VALUE, *inputChars)

private fun proceduralExec(word: String, maxMistakes: Int, vararg inputChars: Char) =
    ByteArrayOutputStream().use { output ->
        ProceduralHangman(
            ByteArrayInputStream(
                inputChars.toLines(trailingBreak = true).toByteArray()
            ),
            output,
            arrayOf(word),
            maxMistakes
        ).exec()

        output.toString()
    }