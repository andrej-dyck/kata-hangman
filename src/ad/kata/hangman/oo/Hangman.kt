package ad.kata.hangman.oo

import java.io.InputStream
import java.io.PrintStream

class Hangman(
    private val words: Words,
    private val maxMisses: MaxMisses,
    private val inputStream: InputStream,
    private val out: PrintStream
) {

    constructor(words: Words, maxMisses: Int, inputStream: InputStream, out: PrintStream) :
        this(words, MaxMisses(maxMisses), inputStream, out)

    fun exec() =
        VerboseHost(
            ComputerHost(words),
            out
        ).take(
            Guesses(inputStream),
            maxMisses
        ).toList()
}
