package ad.kata.hangman

import ad.kata.hangman.oo.Guess
import ad.kata.hangman.oo.Guesses
import ad.kata.hangman.oo.HitOrMiss
import ad.kata.hangman.oo.Host
import ad.kata.hangman.oo.MaxMisses
import ad.kata.hangman.oo.Misses
import ad.kata.hangman.oo.SecretWord
import ad.kata.hangman.oo.Word
import ad.kata.hangman.oo.asObscuredWord
import ad.kata.hangman.oo.chars

/* Strings and Chars */

fun <T> List<T>.toLines(trailingBreak: Boolean = false): String =
    joinToString("\n") + if (trailingBreak) "\n" else ""

fun <T> Array<T>.toLines(trailingBreak: Boolean = false): String =
    toList().toLines(trailingBreak)

fun CharArray.toLines(trailingBreak: Boolean = false) =
    map { it.toString() }.toLines(trailingBreak)

fun CharSequence.nonEmptyLines(): List<String> =
    lines().filter { it.isNotBlank() }

/* Word */

operator fun Word.contains(char: Char) = char in toString()

fun Word.toMinimalGuesses() = Guesses(
    chars().shuffled().asSequence().map { Guess(it) }
)

/* Secret Word */

fun SecretWord.reveal(visible: CharArray) = reveal(visible.toHashSet())

fun SecretWord.length() = asObscuredWord().length()

/* Host */

fun Host.take(guesses: CharSequence) =
    take(guesses.asSequence().map { Guess(it) })

fun Host.take(guesses: CharSequence, maxMisses: Int) =
    take(guesses.asSequence().map { Guess(it) }, maxMisses)

fun Host.take(guesses: Sequence<Guess>) =
    take(guesses, IGNORE_MISSES)

fun Host.take(guesses: Sequence<Guess>, maxMisses: Int) =
    take(guesses, MaxMisses(maxMisses))

fun Host.take(guesses: Sequence<Guess>, maxMisses: MaxMisses) =
    take(Guesses(guesses), maxMisses)

/* Misses */

val IGNORE_MISSES = MaxMisses(Int.MAX_VALUE)

fun missesWithCount(count: Int, maxMisses: Int = 1) =
    missesWithCount(count, MaxMisses(maxMisses).startCounting())

private tailrec fun missesWithCount(count: Int, misses: Misses): Misses =
    if (count <= 0) misses else missesWithCount(count - 1, misses.withAnotherMiss())

operator fun HitOrMiss.Companion.invoke(secretWord: SecretWord, guess: Guess) =
    invoke(secretWord, guess, MaxMisses(1).startCounting())

/* Parameterized Test Arguments */

class ArrowSeparatedStrings(private val list: List<String>) {

    fun toList(): List<String> = list

    companion object {
        @JvmStatic
        fun fromString(csv: String) =
            ArrowSeparatedStrings(csv.split("->").map { it.trim() })
    }
}