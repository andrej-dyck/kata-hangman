package ad.kata.hangman.oo

import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.*

interface Guesses : Sequence<Char>

class PlayerGuesses(private val sequence: Sequence<Char>) : Guesses {

    constructor(inputStream: InputStream, outputStream: OutputStream)
            : this(Scanner(inputStream), PrintStream(outputStream))

    constructor(scanner: Scanner, out: PrintStream) : this(
        sequence = generateSequence {
            out.print("Guess a letter: ")
            scanner.nextChar()
        }
    )

    internal constructor(charSequence: CharSequence) : this(charSequence.asSequence())

    override fun iterator() = sequence.iterator()
}

private tailrec fun Scanner.nextChar(): Char? =
    if (hasNext()) nextLine().firstOrNull() ?: nextChar()
    else null
