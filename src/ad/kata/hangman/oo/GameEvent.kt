package ad.kata.hangman.oo

sealed class GameEvent {

    abstract val revealedWord: Word
}

class GameStarted(
    private val secretWord: SecretWord
) : GameEvent(), TakesGuess {

    override val revealedWord by lazy { secretWord.asObscuredWord() }

    override fun take(guess: Guess) = GuessTaken(secretWord, guess).maybeGameOver()
}

interface TakesGuess {

    fun take(guess: Guess): HitMissOrOver
}

fun GameEvent.takeOr(guess: Guess, otherwise: () -> GameEvent) = when (this) {
    is TakesGuess -> take(guess)
    else -> otherwise()
}

sealed class HitMissOrOver(
    internal val secretWord: SecretWord,
    internal val guesses: List<Guess>
) : GameEvent() {

    init {
        require(guesses.isNotEmpty())
    }

    override val revealedWord by lazy { secretWord.revealWith(guesses) }

    val isHit by lazy { secretWord.isHitWith(guesses.last()) }
    val isRevealed by lazy { secretWord.isRevealedWith(guesses) }
}

class GuessTaken private constructor(
    secretWord: SecretWord,
    guesses: List<Guess>
) : HitMissOrOver(secretWord, guesses), TakesGuess {

    constructor(secretWord: SecretWord, guess: Guess) : this(secretWord, listOf(guess))

    override fun take(guess: Guess): HitMissOrOver =
        GuessTaken(secretWord, guesses + guess).maybeGameOver()

    fun maybeGameOver(): HitMissOrOver =
        if (isRevealed) GameOver(this) else this
}

class GameOver(
    guessTaken: GuessTaken
) : HitMissOrOver(guessTaken.secretWord, guessTaken.guesses) {

    val isWin by lazy { isRevealed }
}
