package ad.kata.hangman.oo

class ObscuredWord(private val word: Word, private val hidden: Char = '?') {

    fun reveal(visible: Set<Char>) =
        word.mapChars { if (it in visible) it else hidden }

    fun isRevealed(visible: Set<Char>) = visible.containsAll(word.chars())
}

fun Word.obscured() = ObscuredWord(this)

fun ObscuredWord.reveal() = reveal(emptySet())
fun ObscuredWord.reveal(visible: CharArray) = reveal(visible.toHashSet())
fun ObscuredWord.reveal(visible: List<Char>) = reveal(visible.toHashSet())

fun ObscuredWord.isRevealed(visible: List<Char>) = isRevealed(visible.toHashSet())

fun ObscuredWord.length() = reveal().length()
