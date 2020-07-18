package ad.kata.hangman.oo

import ad.kata.hangman.missesWithCount
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class MissesTest {

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 5, 7])
    fun `max misses starts count with 0 and max misses value`(maxMisses: Int) {
        assertThat(
            MaxMisses(maxMisses)
                .startCounting()
                .let { it.count to it.maxMisses.value }
        ).isEqualTo(
            0 to maxMisses
        )
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1, 2, 5, 7])
    fun `misses increment count by 1`(count: Int) {
        assertThat(
            missesWithCount(count)
                .withAnotherMiss()
                .count
        ).isEqualTo(
            count + 1
        )
    }

    @ParameterizedTest
    @CsvSource(
        "0, 1, false",
        "1, 1, true",
        "2, 1, true",
        "2, 5, false",
        "5, 5, true"
    )
    fun `attempts are exhausted iff count is larger or equal to max misses`(
        count: Int,
        maxMisses: Int,
        expectedAttemptsExhausted: Boolean
    ) {
        assertThat(
            missesWithCount(count, maxMisses)
                .also { println(it) }
                .attemptsExhausted()
        ).`as`("attempts are exhausted when $count >= $maxMisses").isEqualTo(
            expectedAttemptsExhausted
        )
    }

    @ParameterizedTest
    @ValueSource(ints = [0, -1])
    fun `max misses must be positive`(maxMisses: Int) {
        assertThatCode {
            MaxMisses(maxMisses)
        }.isInstanceOf(
            IllegalArgumentException::class.java
        )
    }
}