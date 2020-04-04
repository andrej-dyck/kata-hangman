package ad.kata.hangman.oo

class ObscuredWord(private val word: Word, private val hidden: Char = '?') {

    fun reveal(visible: Set<Char>) =
        word.mapChars { if (it in visible) it else hidden }

    fun isRevealed(visible: Set<Char>) = visible.containsAll(word.chars())
}

fun Word.obscured() = ObscuredWord(this)
