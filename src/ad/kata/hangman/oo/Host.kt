package ad.kata.hangman.oo

import ad.kata.hangman.kotlinExtensions.runningFold
import ad.kata.hangman.kotlinExtensions.takeWithFirst

interface Host {
    fun obscuredWord(): Word
    fun take(guesses: Guesses): Sequence<GameEvent>
}

class ComputerHost(
    chooseWord: () -> Word
) : Host {

    constructor(word: Word) : this(chooseWord = { word })

    constructor(words: Words) : this(words::random)

    private val secretWord by lazy { chooseWord().toSecret() }

    override fun obscuredWord() = secretWord.asObscuredWord()

    override fun take(guesses: Guesses) =
        guesses.runningFold(
            GameStarted(secretWord) as GameEvent
        ) { event: GameEvent, guess: Char ->
            event.takeOr(guess) { throw IllegalStateException() }
        }.takeWithFirst {
            it is GameOver
        }.drop(1) // ignore game started for now
}
