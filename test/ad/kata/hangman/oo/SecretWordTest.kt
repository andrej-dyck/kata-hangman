package ad.kata.hangman.oo

import ad.kata.hangman.contains
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
                .asObscuredWord()
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
                .reveal(letters = word.chars().shuffled())
        ).isEqualTo(
            word
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "a", "book", "called", "elegant", "objects"])
    fun `secret word is still hidden when none of its letters are guessed`(word: Word) {
        assertThat(
            word.toSecret()
                .reveal(letters = ('a'..'z').filter { it !in word })
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
                .reveal(letters = setOf('a', 'b', 'c'))
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
                .isRevealed(letters = word.chars().shuffled())
        ).`as`("word is revealed").isEqualTo(
            true
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `secret word is not revealed when at least one letter of the word is not guessed`(word: Word) {
        assertThat(
            word.toSecret()
                .isRevealed(letters = word.chars().shuffled().drop(1))
        ).`as`("word is revealed").isEqualTo(
            false
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `each letter of the word is a hit`(word: Word) {
        word.chars().forEach { letter ->
            assertThat(
                word.toSecret().isHit(letter)
            ).`as`("$letter must be a hit").isTrue()
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["a", "book", "called", "elegant", "objects"])
    fun `each letter that is not one of the word is a miss`(word: Word) {
        val lettersNotInWord = 'a'..'z' subtract word.chars()

        lettersNotInWord.forEach { letter ->
            assertThat(
                word.toSecret().isMiss(letter)
            ).`as`("$letter must be a miss").isTrue()
        }
    }
}
