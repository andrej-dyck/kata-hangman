package ad.kata.hangman.oo

import ad.kata.hangman.kotlinExtensions.takeWithFirst
import java.io.PrintStream

interface Host {
    fun take(guesses: Guesses): Sequence<Word>
}

class ComputerHost(
    private val secretWord: SecretWord
) : Host {

    constructor(word: Word) : this(word.toSecret())

    override fun take(guesses: Guesses): Sequence<Word> {
        val guessed = mutableListOf<Char>()

        return sequenceOf(secretWord.asObscuredWord()) + guesses
            .map {
                guessed.add(it)
                secretWord.reveal(guessed)
            }
            .takeWithFirst {
                secretWord.isRevealed(guessed)
            }
    }
}
