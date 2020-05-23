package ad.kata.hangman

import ad.kata.hangman.oo.SecretWord
import ad.kata.hangman.oo.Word
import ad.kata.hangman.oo.asObscuredWord

/* Strings and Chars */

fun <T> List<T>.toLines(trailingBreak: Boolean = false): String =
    joinToString("\n") + (if (trailingBreak) "\n" else "")

fun <T> Array<T>.toLines(trailingBreak: Boolean = false): String =
    toList().toLines(trailingBreak)

fun CharArray.toLines(trailingBreak: Boolean = false) =
    map { it.toString() }.toLines(trailingBreak)

fun String.shuffle() =
    toMutableList().also { it.shuffle() }.joinToString("")

/* Word */

operator fun Word.contains(char: Char) = char in toString()

/* Secret Word */

fun SecretWord.reveal(visible: CharArray) = reveal(visible.toHashSet())

fun SecretWord.length() = asObscuredWord().length()

/* Parameterized Test Arguments */

class ArrowSeparatedStrings(private val list: List<String>) {

    fun toList(): List<String> = list

    companion object {
        @JvmStatic
        fun fromString(csv: String) =
            ArrowSeparatedStrings(csv.split("->").map { it.trim() })
    }
}