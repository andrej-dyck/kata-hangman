package ad.kata.hangman.oo

import java.io.InputStream
import java.io.PrintStream

class Hangman(
    private val words: Words,
    private val inputStream: InputStream,
    private val out: PrintStream
) {

    fun exec() =
        VerboseHost(
            ComputerHost(words),
            out
        ).take(
            Guesses(inputStream)
        ).toList()
}
