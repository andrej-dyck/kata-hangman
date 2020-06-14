package ad.kata.hangman.oo

sealed class GameEvent {

    abstract val revealedWord: Word
}

class GameStarted(private val secretWord: SecretWord) : GameEvent(), TakesGuess {

    override val revealedWord by lazy { secretWord.asObscuredWord() }

    override fun take(guess: Char) = GuessTaken(secretWord, guess).maybeGameOver()
}

interface TakesGuess {

    fun take(guess: Char): HitMissOrOver
}

fun GameEvent.takeOr(guess: Char, otherwise: () -> GameEvent) = when (this) {
    is TakesGuess -> take(guess)
    else -> otherwise()
}

sealed class HitMissOrOver(
    internal val secretWord: SecretWord,
    internal val guessedLetters: List<Char>
) : GameEvent() {

    init {
        require(guessedLetters.isNotEmpty())
    }

    override val revealedWord by lazy { secretWord.reveal(guessedLetters) }

    val isHit by lazy { secretWord.isHit(guessedLetters.last()) }
    val isRevealed by lazy { secretWord.isRevealed(guessedLetters) }
}

class GuessTaken private constructor(
    secretWord: SecretWord,
    guessedLetters: List<Char>
) : HitMissOrOver(secretWord, guessedLetters), TakesGuess {

    constructor(secretWord: SecretWord, guess: Char) : this(secretWord, listOf(guess))

    override fun take(guess: Char): HitMissOrOver =
        GuessTaken(secretWord, guessedLetters + guess).maybeGameOver()

    fun maybeGameOver(): HitMissOrOver =
        if (isRevealed) GameOver(this) else this
}

class GameOver(guessTaken: GuessTaken) : HitMissOrOver(guessTaken.secretWord, guessTaken.guessedLetters) {

    val isWin by lazy { isRevealed }
}
