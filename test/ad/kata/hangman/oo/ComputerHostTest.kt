package ad.kata.hangman.oo

import ad.kata.hangman.ArrowSeparatedStrings
import ad.kata.hangman.shuffle
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class ComputerHostTest {

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `can show secret all-? word`(word: Word) {
        assertThat(
            ComputerHost(word).obscuredWord()
        ).isEqualTo(
            word.toSecret().asObscuredWord()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `takes guesses until word is revealed`(word: Word) {
        val guesses = word.toString().shuffle()

        assertThat(
            ComputerHost(word)
                .take(PlayerGuesses(guesses))
                .last()
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
                .take(PlayerGuesses(guesses))
                .map { it.toString() }
                .toList()
        ).containsExactlyElementsOf(
            expectedReveals.toList()
        )
    }

    @Test
    fun `with no guesses reveals nothing`() {
        assertThat(
            ComputerHost(Word("something"))
                .take(PlayerGuesses(emptySequence()))
                .toList()
        ).isEmpty()
    }
}

