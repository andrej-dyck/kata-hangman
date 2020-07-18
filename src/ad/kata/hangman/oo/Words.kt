package ad.kata.hangman.oo

interface Words {

    fun random(): Word
}

class WordList(private val words: Set<Word>) : Words {

    init {
        require(words.isNotEmpty()) { "list of words must not be empty" }
    }

    constructor(words: List<Word>) : this(words.toSet())

    override fun random() = words.shuffled().first()
}
