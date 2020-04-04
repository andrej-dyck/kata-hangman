package ad.kata.hangman

import ad.kata.hangman.oo.ObscuredWord
import ad.kata.hangman.oo.Word

/* Strings and Chars */

fun <T> List<T>.toLines(trailingBreak: Boolean = false): String =
    joinToString("\n") + (if (trailingBreak) "\n" else "")

fun <T> Array<T>.toLines(trailingBreak: Boolean = false): String =
    toList().toLines(trailingBreak)

fun CharArray.toLines(trailingBreak: Boolean = false) =
    map { it.toString() }.toLines(trailingBreak)

/* Word */

operator fun Word.contains(char: Char) = char in toString()

/* Obscured Word */

fun ObscuredWord.reveal() = reveal(emptySet())
fun ObscuredWord.reveal(visible: CharArray) = reveal(visible.toHashSet())
fun ObscuredWord.reveal(visible: List<Char>) = reveal(visible.toHashSet())

fun ObscuredWord.isRevealed(visible: List<Char>) = isRevealed(visible.toHashSet())

fun ObscuredWord.length() = reveal().length()

