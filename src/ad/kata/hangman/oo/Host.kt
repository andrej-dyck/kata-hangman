package ad.kata.hangman.oo

import ad.kata.hangman.kotlinExtensions.runningFold
import ad.kata.hangman.kotlinExtensions.takeWithFirst

interface Host {
    fun take(guesses: Guesses): Sequence<GameEvent>
}

class ComputerHost(
    private val chooseWord: () -> Word
) : Host {

    constructor(word: Word) : this(chooseWord = { word })

    constructor(words: Words) : this(words::random)

    override fun take(guesses: Guesses) =
        guesses.runningFold(
            GameStarted(newSecretWord()) as GameEvent
        ) { event: GameEvent, guess: Guess ->
            event.takeOr(guess) { throw IllegalStateException() }
        }.takeWithFirst {
            it is GameOver
        }

    private fun newSecretWord() = chooseWord().toSecret()
}
