package ad.kata.hangman

import ad.kata.hangman.oo.ObscuredWord
import ad.kata.hangman.oo.Word
import ad.kata.hangman.oo.view
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import ad.kata.hangman.procedural.Hangman as ProceduralHangman

class RefactoringRegressionTests {

    @Nested
    inner class ObscuresWordsTheSameWay {

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
        fun `obscured word is depicted with ?-marks`(
            word: String,
            guesses: String
        ) {
            assertThat(
                word.map { if (it in visible) it else '?' }
                    .joinToString("") /* TODO EXPERIMENTTAL IMPLEMENTATION */
            ).isEqualTo(
                proceduralExec(word.toString(), *guesses.toCharArray())
                    .lines()
                    .last { it.startsWith("The word: ") }
                    .replace("The word: ", "")
            )
        }
    }
}

private fun proceduralExec(word: String, vararg inputChars: Char) =
    proceduralExec(word, Int.MAX_VALUE, *inputChars)

private fun proceduralExec(word: String, maxMistakes: Int, vararg inputChars: Char) =
    ByteArrayOutputStream().use { output ->
        ProceduralHangman(
            ByteArrayInputStream(
                inputChars.joinToString("") { "$it\n" }.toByteArray()
            ),
            output,
            arrayOf(word),
            maxMistakes
        ).exec()

        output.toString()
    }