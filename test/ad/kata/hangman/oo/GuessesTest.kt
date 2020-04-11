package ad.kata.hangman.oo

import ad.kata.hangman.toLines
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream

abstract class GuessesTest {

    @Test
    fun `next is the first character from the input`() {
        assertThat(
            guesses('a', 'b', 'c').first()
        ).isEqualTo(
            'a'
        )
    }

    @Test
    fun `subsequent next are the characters of the input in order`() {
        assertThat(
            guesses('a', 'b', 'c').take(3).toList()
        ).containsExactly(
            'a', 'b', 'c'
        )
    }

    abstract fun guesses(vararg input: Char): Guesses
}

class PlayerGuessesTest : GuessesTest() {

    @ParameterizedTest
    @CsvSource(
        "a,a",
        "abc,a",
        "book,b"
    )
    fun `next takes only first character from user input line`(
        nextLine: String,
        expectedChar: Char
    ) {
        assertThat(
            guesses(nextLine).first()
        ).isEqualTo(
            expectedChar
        )
    }

    @Test
    fun `prints input request before next`() {
        val out = ByteArrayOutputStream()

        guesses(out).firstOrNull()

        assertThat(
            out.toString()
        ).startsWith(
            "Guess a letter: "
        )
    }

    @Test
    fun `empty lines do not count as input`() {
        assertThat(
            guesses("", "", "", "a").first()
        ).isEqualTo(
            'a'
        )
    }

    override fun guesses(vararg input: Char) =
        guesses(input.toLines())

    private fun guesses(vararg input: String) =
        guesses(ByteArrayOutputStream(), *input)

    private fun guesses(outputStream: OutputStream, vararg input: String) =
        PlayerGuesses(
            ByteArrayInputStream(input.toLines(trailingBreak = true).toByteArray()),
            outputStream
        )
}