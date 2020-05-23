package ad.kata.hangman.oo

class SecretWord(private val word: Word, private val hidden: Char = '?') {

    fun reveal(visible: Set<Char>) =
        word.mapChars { if (it in visible) it else hidden }

    fun isRevealed(visible: Set<Char>) =
        visible.containsAll(word.chars())
}

fun Word.toSecret() = SecretWord(this)

fun SecretWord.asObscuredWord() = reveal(emptySet())
fun SecretWord.reveal(visible: List<Char>) = reveal(visible.toHashSet())
fun SecretWord.isRevealed(visible: List<Char>) = isRevealed(visible.toHashSet())