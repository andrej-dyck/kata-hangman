package ad.kata.hangman.oo

import ad.kata.hangman.invoke
import ad.kata.hangman.missesWithCount
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class HitOrMissTest {

    @Test
    fun `guess is a hit when letter is part of the secret word`() {
        assertThat(
            HitOrMiss(Word("a").toSecret(), Guess('a'))
        ).isInstanceOf(
            Hit::class.java
        )
    }

    @Test
    fun `guess is a miss when letter is not part of the secret word`() {
        assertThat(
            HitOrMiss(Word("a").toSecret(), Guess('x'))
        ).isInstanceOf(
            Miss::class.java
        )
    }

    @Test
    fun `a hit does not increment misses count`() {
        assertThat(
            HitOrMiss(Word("a").toSecret(), Guess('a')).misses.count
        ).`as`("number of misses").isEqualTo(
            0
        )
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 5, 7])
    fun `a miss increments misses count by 1`(count: Int) {
        val misses = missesWithCount(count)

        assertThat(
            HitOrMiss(Word("a").toSecret(), Guess('x'), misses).misses.count
        ).`as`("number of misses").isEqualTo(
            misses.count + 1
        )
    }
}