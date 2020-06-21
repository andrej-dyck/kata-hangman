package ad.kata.hangman.oo

import java.io.OutputStream
import java.io.PrintStream

class VerboseHost(
    private val host: Host,
    private val out: PrintStream
) : Host {

    constructor(host: Host, out: OutputStream) : this(host, PrintStream(out))

    override fun take(guesses: Guesses) =
        host.take(promptFor(guesses)).onEach(this::print)

    private fun promptFor(guesses: Guesses) = Guesses(
        generateSequence {
            out.print("Guess a letter: ")
        }.zip(guesses).map { (_, guess) ->
            guess
        }
    )

    private fun print(event: GameEvent) {
        VerboseGameEvent(event).printOn(out)
        out.println()
    }
}

class VerboseGameEvent(event: GameEvent) {

    private val feedback by lazy {
        when (event) {
            is GameStarted -> "A new Hangman game"
            is GuessTaken -> if (event.isHit) "Hit!" else "Missed."
            is GameOver -> if (event.isWin) "Hit! You win!" else "Missed. You lost."
        }
    }

    private val revealedWord by lazy {
        event.revealedWord
    }

    fun printOn(out: PrintStream) {
        out.println(feedback)
        out.println("The word: $revealedWord")
    }
}
