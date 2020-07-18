package ad.kata.hangman.oo

sealed class GameEvent {

    abstract val revealedWord: Word
}

class GameStarted(
    private val secretWord: SecretWord,
    val maxMisses: MaxMisses
) : GameEvent(), TakesGuess {

    override val revealedWord by lazy { secretWord.asObscuredWord() }

    override fun take(guess: Guess) = GuessTaken(secretWord, maxMisses, guess)
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
    internal val hitsAndMisses: List<HitOrMiss>
) : GameEvent() {

    init {
        require(hitsAndMisses.isNotEmpty())
    }

    private val guesses by lazy { hitsAndMisses.map { it.guess } }

    override val revealedWord by lazy { secretWord.revealWith(guesses) }
    val isRevealed by lazy { secretWord.isRevealedWith(guesses) }

    val hitOrMiss by lazy { hitsAndMisses.last() }
}

class GuessTaken private constructor(
    secretWord: SecretWord,
    hitsAndMisses: List<HitOrMiss>
) : HitMissOrOver(secretWord, hitsAndMisses), TakesGuess {

    private constructor(secretWord: SecretWord, maxMisses: MaxMisses, guess: Guess) :
        this(secretWord, listOf(), maxMisses.startCounting(), guess)

    private constructor(secretWord: SecretWord, hitsAndMisses: List<HitOrMiss>, misses: Misses, guess: Guess) :
        this(secretWord, hitsAndMisses = hitsAndMisses + HitOrMiss(secretWord, guess, misses))

    private constructor(guessTaken: GuessTaken, guess: Guess) :
        this(guessTaken.secretWord, guessTaken.hitsAndMisses, guessTaken.hitOrMiss.misses, guess)

    override fun take(guess: Guess) =
        GuessTaken(this, guess).maybeGameOver()

    private fun maybeGameOver() =
        if (isRevealed || attemptsExhausted()) GameOver(this) else this

    private fun attemptsExhausted() =
        hitOrMiss.misses.attemptsExhausted()

    companion object {
        operator fun invoke(secretWord: SecretWord, maxMisses: MaxMisses, guess: Guess) =
            GuessTaken(secretWord, maxMisses, guess).maybeGameOver()
    }
}

class GameOver(
    guessTaken: GuessTaken
) : HitMissOrOver(guessTaken.secretWord, guessTaken.hitsAndMisses) {

    val isWin by lazy { isRevealed }
}
