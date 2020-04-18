package ad.kata.hangman.oo

import ad.kata.hangman.contains
import ad.kata.hangman.isRevealed
import ad.kata.hangman.length
import ad.kata.hangman.reveal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class SecretWordTest {

    @ParameterizedTest
    @CsvSource(
        "a, x, ?",
        "a, a, a",
        "book, o, ?oo?",
        "book, x, ????",
        "book, ob, boo?",
        "book, xob, boo?",
        "book, kob, book",
        "elegant, e, e?e????",
        "objects, x, ???????",
        "objects, st, ?????ts"
    )
    fun `secret word reveals all appearances of correctly guessed letters`(
        word: Word,
        guesses: String,
        expectedObscuredWord: Word
    ) {
        assertThat(
            word.toSecret()
                .reveal(visible = guesses.toCharArray())
        ).isEqualTo(
            expectedObscuredWord
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `secret word has the same length as the word`(word: Word) {
        assertThat(
            word.toSecret()
                .length()
        ).isEqualTo(
            word.length()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `secret word comprises only ? as letters`(word: Word) {
        assertThat(
            word.toSecret()
                .reveal()
                .toString()
        ).matches(
            "\\?*".toPattern()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `secret word reveals word when all its letters are guessed`(word: Word) {
        assertThat(
            word.toSecret()
                .reveal(visible = word.chars().shuffled())
        ).isEqualTo(
            word
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `secret word is still hidden when none of its letters are guessed`(word: Word) {
        assertThat(
            word.toSecret()
                .reveal(visible = ('a'..'z').filter { it !in word })
                .toString()
        ).matches(
            "\\?*".toPattern()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `secret word reveals exactly those letters that are guessed correctly`(word: Word) {
        assertThat(
            word.toSecret()
                .reveal(visible = setOf('a', 'b', 'c'))
                .toString()
        ).matches(
            "[a, b, c, \\?]*".toPattern()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `secret word is revealed when at least all letters of the word are guessed`(word: Word) {
        assertThat(
            word.toSecret()
                .isRevealed(visible = word.chars().shuffled())
        ).isEqualTo(
            true
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `secret word is not revealed when at least one letter of the word is not guessed`(word: Word) {
        assertThat(
            word.toSecret()
                .isRevealed(visible = word.chars().shuffled().drop(1))
        ).isEqualTo(
            false
        )
    }
}