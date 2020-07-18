package ad.kata.hangman.oo

import java.io.OutputStream
import java.io.PrintStream

class VerboseHost(
    private val host: Host,
    private val out: PrintStream
) : Host {

    constructor(host: Host, out: OutputStream) : this(host, PrintStream(out))

    override fun take(guesses: Guesses, maxMisses: MaxMisses) =
        host.take(promptFor(guesses), maxMisses).onEach(this::print)

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
            is GameStarted -> "A new Hangman game. Max mistakes allowed: ${event.maxMissesAllowed}"
            is GuessTaken -> when (val it = event.hitOrMiss) {
                is Hit -> "Hit!"
                is Miss -> "Missed, mistake #${it.numberOfMisses} out of ${it.maxMissesAllowed}."
            }
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

internal val GameStarted.maxMissesAllowed
    get() = maxMisses.value

internal val HitOrMiss.numberOfMisses
    get() = misses.count

internal val HitOrMiss.maxMissesAllowed
    get() = misses.maxMisses.value
