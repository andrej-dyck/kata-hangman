package ad.kata.hangman.oo

import ad.kata.hangman.ArrowSeparatedStrings
import ad.kata.hangman.nonEmptyLines
import ad.kata.hangman.take
import ad.kata.hangman.toMinimalGuesses
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class VerboseHostTest {

    @Test
    fun `starts by telling its a new game`() {
        val verboseHost = verboseHostSpy(Word("word"))

        verboseHost.take(guesses = emptySequence()).toList()

        assertThat(
            verboseHost
                .nonEmptyLines()
                .first()
        ).startsWith(
            "A new Hangman game"
        )
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 5, 7])
    fun `starts by telling max allowed misses`(maxMisses: Int) {
        val verboseHost = verboseHostSpy(Word("word"))

        verboseHost.take(guesses = emptySequence(), maxMisses = maxMisses).toList()

        assertThat(
            verboseHost
                .nonEmptyLines()
                .first()
        ).endsWith(
            "Max mistakes allowed: $maxMisses"
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `shows obscured word as the last introduction line`(word: Word) {
        val verboseHost = verboseHostSpy(word)

        verboseHost.take(guesses = emptySequence()).toList()

        assertThat(
            verboseHost
                .nonEmptyLines()
                .take(numberOfGameStartedLines)
                .last()
        ).isEqualTo(
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

    @ParameterizedTest
    @CsvSource(
        "hangman, hxanmxg, Hit -> Missed -> Hit -> Hit -> Hit -> Missed -> Hit",
        "book, xoxbk, Missed -> Hit -> Missed -> Hit -> Hit"
    )
    fun `gives feedback after each guess`(word: Word, guesses: String, expectedHitsAndMisses: ArrowSeparatedStrings) {

        val verboseHost = verboseHostSpy(word)

        verboseHost.take(guesses).toList()

        assertThat(
            verboseHost
                .nonEmptyLines()
                .map { it.removePrefix("Guess a letter: ") }
                .filter { it.startsWith("Hit") || it.startsWith("Missed") }
                .map { if (it.startsWith("Hit")) "Hit" else "Missed" }
        ).containsExactlyElementsOf(
            expectedHitsAndMisses.toList()
        )
    }

    @ParameterizedTest
    @CsvSource(
        "hangman, 5, xxx, mistake #1 out of 5 -> mistake #2 out of 5 -> mistake #3 out of 5",
        "book, 2, x, mistake #1 out of 2"
    )
    fun `gives feedback about count and max mistakes allowed after each miss`(
        word: Word,
        maxMisses: Int,
        guesses: String,
        expectedHitsAndMisses: ArrowSeparatedStrings
    ) {
        val verboseHost = verboseHostSpy(word)

        verboseHost.take(guesses, maxMisses).toList()

        assertThat(
            verboseHost
                .nonEmptyLines()
                .map { it.removePrefix("Guess a letter: ") }
                .filter { it.startsWith("Missed") }
                .map { it.removePrefix("Missed").trim(' ', ',', '.') }
        ).containsExactlyElementsOf(
            expectedHitsAndMisses.toList()
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