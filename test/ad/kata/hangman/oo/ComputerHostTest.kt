package ad.kata.hangman.oo

import ad.kata.hangman.ArrowSeparatedStrings
import ad.kata.hangman.take
import ad.kata.hangman.toMinimalGuesses
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class ComputerHostTest {

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `can select a random word from words`(randomWord: Word) {
        assertThat(
            ComputerHost(
                WordList(listOf(randomWord))
            ).take(
                randomWord.toMinimalGuesses()
            ).last().revealedWord
        ).isEqualTo(
            randomWord
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `shows secret all-? word as first event`(word: Word) {
        assertThat(
            ComputerHost(word)
                .take(emptySequence())
                .first()
                .revealedWord
        ).isEqualTo(
            word.toSecret().asObscuredWord()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `takes guesses until word is revealed`(word: Word) {
        val guesses = word.toMinimalGuesses()

        assertThat(
            ComputerHost(word)
                .take(guesses)
                .last()
                .revealedWord
        ).isEqualTo(
            word
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
        assertThat(
            ComputerHost(word)
                .take(guesses)
                .drop(1) // ignore game-started reveal of the word
                .map { it.revealedWord.toString() }
                .toList()
        ).containsExactlyElementsOf(
            expectedReveals.toList()
        )
    }
}

