package ad.kata.hangman.oo

import java.io.InputStream
import java.util.Scanner

class Guesses(private val sequence: Sequence<Guess>) : Sequence<Guess> {

    constructor(inputStream: InputStream) : this(Scanner(inputStream))

    constructor(scanner: Scanner) : this(
        sequence = generateSequence {
            scanner.nextChar()
        }.map {
            Guess(it)
        }
    )

    override fun iterator() = sequence.iterator()
}

inline class Guess(val letter: Char)

private tailrec fun Scanner.nextChar(): Char? =
    hasNext()
        .takeIf { it }
        ?.let { nextLine().firstOrNull() }
        ?: nextChar()
