package ad.kata.hangman.oo

sealed class HitOrMiss(val guess: Guess, val misses: Misses) {

    companion object {

        operator fun invoke(secretWord: SecretWord, guess: Guess, previousMisses: Misses) =
            if (secretWord.isHitWith(guess)) {
                Hit(guess, previousMisses)
            } else {
                Miss(guess, previousMisses)
            }
    }
}

class Hit(guess: Guess, misses: Misses) : HitOrMiss(guess, misses)
class Miss(guess: Guess, previousMisses: Misses) : HitOrMiss(guess, misses = previousMisses.withAnotherMiss())
