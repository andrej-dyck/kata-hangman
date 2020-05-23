package ad.kata.hangman.kotlinExtensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SequenceTakeWithFirstTest {

    @Test
    fun `takes elements until an element satisfies the condition including that element`() {
        assertThat(
            (1..2001).asSequence().takeWithFirst { it == 17 }.toList()
        ).isEqualTo(
            (1..17).toList()
        )
    }

    @Test
    fun `takes nothing when sequence is empty`() {
        assertThat(
            emptySequence<Any>().takeWithFirst { false }.toList()
        ).isEmpty()
    }

    @ParameterizedTest
    @CsvSource("1, 1", "1, 3", "1, 7", "1, 2001")
    fun `takes all elements when condition is always false`(from: Int, to: Int) {
        val sequence = (from..to).asSequence()

        assertThat(
            sequence.takeWithFirst { false }.toList()
        ).isEqualTo(
            sequence.toList()
        )
    }

    @ParameterizedTest
    @CsvSource("1, 1", "1, 3", "1, 7", "1, 2001")
    fun `takes first element of non-empty sequence when condition is always true`(from: Int, to: Int) {
        val sequence = (from..to).asSequence()

        assertThat(
            sequence.takeWithFirst { true }.toList()
        ).hasOnlyOneElementSatisfying {
            assertThat(it).isEqualTo(sequence.first())
        }
    }
}