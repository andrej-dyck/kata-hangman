package ad.kata.hangman.oo

import java.io.InputStream
import java.util.Scanner

class Guesses(private val sequence: Sequence<Char>) : Sequence<Char> {

    constructor(scanner: Scanner) : this(
        sequence = generateSequence {
            scanner.nextChar()
        }
    )

    constructor(inputStream: InputStream) : this(Scanner(inputStream))

    override fun iterator() = sequence.iterator()
}

private tailrec fun Scanner.nextChar(): Char? =
    hasNext()
        .takeIf { it }
        ?.let { nextLine().firstOrNull() }
        ?: nextChar()
