package ad.kata.hangman.oo

import ad.kata.hangman.kotlinExtensions.runningFold
import ad.kata.hangman.kotlinExtensions.takeWithFirst

interface Host {
    fun take(guesses: Guesses, maxMisses: MaxMisses): Sequence<GameEvent>
}

class ComputerHost(
    private val chooseWord: () -> Word
) : Host {

    constructor(word: Word) : this(chooseWord = { word })

    constructor(words: Words) : this(words::random)

    override fun take(guesses: Guesses, maxMisses: MaxMisses) =
        guesses.runningFold(
            GameStarted(newSecretWord(), maxMisses) as GameEvent
        ) { event: GameEvent, guess: Guess ->
            event.takeOr(guess) { throw IllegalStateException("game over") }
        }.takeWithFirst {
            it is GameOver
        }

    private fun newSecretWord() = chooseWord().toSecret()
}
