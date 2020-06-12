package ad.kata.hangman.oo

import ad.kata.hangman.kotlinExtensions.takeWithFirst

interface Host {
    fun obscuredWord(): Word
    fun take(guesses: Guesses): Sequence<Word>
}

class ComputerHost(
    private val secretWord: SecretWord
) : Host {

    constructor(word: Word) : this(word.toSecret())

    override fun obscuredWord() = secretWord.asObscuredWord()

    override fun take(guesses: Guesses): Sequence<Word> {
        val letters = mutableListOf<Char>()

        return guesses
            .map {
                letters.add(it)
                secretWord.reveal(letters)
            }
            .takeWithFirst {
                secretWord.isRevealed(letters)
            }
    }
}
