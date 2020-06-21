package ad.kata.hangman.oo

inline class Word(private val value: String) {

    constructor(chars: List<Char>) : this(chars.joinToString(""))

    fun length() = value.length

    override fun toString() = value
}

fun Word.chars() = toString().toCharArray().toHashSet()

fun Word.mapChars(transform: (Char) -> Char): Word = Word(toString().map(transform))
