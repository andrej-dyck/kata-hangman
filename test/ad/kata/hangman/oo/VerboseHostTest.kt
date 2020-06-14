package ad.kata.hangman.oo

import ad.kata.hangman.ArrowSeparatedStrings
import ad.kata.hangman.nonEmptyLines
import ad.kata.hangman.take
import ad.kata.hangman.toMinimalGuesses
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class VerboseHostTest {

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `shows obscured word before anything else`(word: Word) {
        val verboseHost = verboseHostSpy(word)

        verboseHost.take(guesses = emptySequence()).toList()

        assertThat(
            verboseHost
                .nonEmptyLines()
                .take(numberOfGameStartedLines)
        ).containsExactly(
            "A new Hangman game",
            "The word: ${word.toSecret().asObscuredWord()}"
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `asks for a guess before any guess`(word: Word) {
        val verboseHost = verboseHostSpy(word)

        verboseHost.take(guesses = emptySequence()).toList()

        assertThat(
            verboseHost
                .nonEmptyLines()
                .drop(numberOfGameStartedLines)
                .first()
        ).startsWith(
            "Guess a letter: "
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `asks for a guess before every guess`(word: Word) {
        val verboseHost = verboseHostSpy(word)
        val minimalGuesses = word.toMinimalGuesses()

        verboseHost.take(minimalGuesses).toList()

        assertThat(
            verboseHost
                .nonEmptyLines()
                .filter { it.startsWith("Guess a letter: ") }
                .size
        ).`as`("number of prompts for $word").isEqualTo(
            minimalGuesses.count()
        )
    }

    @ParameterizedTest
    @CsvSource(
        "hangman, hxanmg, h?????? -> h?????? -> ha???a? -> han??an -> han?man -> hangman",
        "book, xobk, ???? -> ?oo? -> boo? -> book",
        "elegant, exlgnta, e?e???? -> e?e???? -> ele???? -> eleg??? -> eleg?n? -> eleg?nt -> elegant",
        "objects, objects, o?????? -> ob????? -> obj???? -> obje??? -> objec?? -> object? -> objects"
    )
    fun `reveals word after each guess`(word: Word, guesses: String, expectedReveals: ArrowSeparatedStrings) {

        val verboseHost = verboseHostSpy(word)

        verboseHost.take(guesses).toList()

        assertThat(
            verboseHost
                .nonEmptyLines()
                .drop(numberOfGameStartedLines)
                .filter { it.startsWith("The word: ") }
                .map { it.removePrefix("The word: ") }
        ).containsExactlyElementsOf(
            expectedReveals.toList()
        )
    }

    private val numberOfGameStartedLines = 2

    private fun verboseHostSpy(word: Word) = VerboseHostSpy(word)

    private class VerboseHostSpy(
        word: Word,
        private val out: OutputStream = ByteArrayOutputStream()
    ) : Host by VerboseHost(
        ComputerHost(word),
        out
    ) {

        fun nonEmptyLines(): List<String> = out.toString().nonEmptyLines()
    }
}