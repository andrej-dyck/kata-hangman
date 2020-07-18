package ad.kata.hangman.oo

class Misses private constructor(val count: Int, val maxMisses: MaxMisses) {

    constructor(maxMisses: MaxMisses) : this(count = 0, maxMisses = maxMisses)

    fun withAnotherMiss() = Misses(count + 1, maxMisses)

    fun attemptsExhausted() = count >= maxMisses.value
}

data class MaxMisses(val value: Int) {

    init {
        require(value >= 1) { "expected max misses to be >= 1" }
    }

    fun startCounting() = Misses(this)
}
