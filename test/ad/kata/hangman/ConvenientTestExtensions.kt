package ad.kata.hangman

import ad.kata.hangman.oo.SecretWord
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

/* Secret Word */

fun SecretWord.reveal() = reveal(emptySet())
fun SecretWord.reveal(visible: CharArray) = reveal(visible.toHashSet())
fun SecretWord.reveal(visible: List<Char>) = reveal(visible.toHashSet())

fun SecretWord.isRevealed(visible: List<Char>) = isRevealed(visible.toHashSet())

fun SecretWord.length() = reveal().length()

