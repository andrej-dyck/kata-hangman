package ad.kata.hangman.oo

import java.io.OutputStream
import java.io.PrintStream

class VerboseHost(
    private val host: Host,
    private val out: PrintStream
) : Host {

    constructor(host: Host, out: OutputStream) : this(host, PrintStream(out))

    override fun obscuredWord() =
        host.obscuredWord().also(this::print)

    override fun take(guesses: Guesses) =
        host.take(askFor(guesses)).onEach(this::print)

    private fun askFor(guesses: Guesses) = Guesses(
        generateSequence {
            out.print("Guess a letter: ")
        }.zip(guesses).map {
            it.second
        }
    )

    private fun print(word: Word) {
        out.println()
        out.println("The word: $word")
        out.println()
    }
}