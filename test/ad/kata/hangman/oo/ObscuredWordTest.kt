package ad.kata.hangman.oo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class ObscuredWordTest {

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
    fun `obscured word reveals all appearances of correctly guessed letters`(
        word: Word,
        guesses: String,
        expectedObscuredWord: Word
    ) {
        assertThat(
            word.obscured()
                .reveal(visible = guesses.toCharArray())
        ).isEqualTo(
            expectedObscuredWord
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `obscured word has the same length as the word`(word: Word) {
        assertThat(
            word.obscured()
                .length()
        ).isEqualTo(
            word.length()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `obscured word comprises only ? as letters`(word: Word) {
        assertThat(
            word.obscured()
                .reveal()
                .toString()
        ).matches(
            "\\?*".toPattern()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `obscured word reveals word when all its letters are guessed`(word: Word) {
        assertThat(
            word.obscured()
                .reveal(visible = word.chars().shuffled())
        ).isEqualTo(
            word
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `obscured word is still hidden when none of its letters are guessed`(word: Word) {
        assertThat(
            word.obscured()
                .reveal(visible = ('a'..'z').filter { it !in word })
                .toString()
        ).matches(
            "\\?*".toPattern()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `obscured word reveals exactly those letters that are guessed correctly`(word: Word) {
        assertThat(
            word.obscured()
                .reveal(visible = setOf('a', 'b', 'c'))
                .toString()
        ).matches(
            "[a, b, c, \\?]*".toPattern()
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `obscured word is revealed when at least all letters of the word are guessed`(word: Word) {
        assertThat(
            word.obscured()
                .isRevealed(visible = word.chars().shuffled())
        ).isEqualTo(
            true
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `obscured word is not revealed when at least one letter of the word is not guessed`(word: Word) {
        assertThat(
            word.obscured()
                .isRevealed(visible = word.chars().shuffled().drop(1))
        ).isEqualTo(
            false
        )
    }
}

private operator fun Word.contains(char: Char) = char in toString()