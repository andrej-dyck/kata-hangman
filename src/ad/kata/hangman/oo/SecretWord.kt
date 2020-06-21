package ad.kata.hangman.oo

class SecretWord(private val word: Word, private val hidden: Char = '?') {

    private val wordChars by lazy { word.chars() }

    fun reveal(letters: Set<Char>) =
        word.mapChars { if (it in letters) it else hidden }

    fun isRevealed(letters: Set<Char>) =
        letters.containsAll(wordChars)

    fun isHit(letter: Char) =
        letter in wordChars
}

fun Word.toSecret() = SecretWord(this)

fun SecretWord.asObscuredWord() = reveal(emptySet())
fun SecretWord.reveal(letters: List<Char>) = reveal(letters.toHashSet())
fun SecretWord.isRevealed(letters: List<Char>) = isRevealed(letters.toHashSet())
fun SecretWord.isMiss(letter: Char) = !isHit(letter)
