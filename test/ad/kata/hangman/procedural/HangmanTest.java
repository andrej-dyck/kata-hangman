package ad.kata.hangman.procedural;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.assertThat;

public final class HangmanTest {

    @Test
    public void failsAfterManyWrongAttempts() throws Exception {
        final ByteArrayInputStream input = new ByteArrayInputStream(
            "a\na\na\na\na\n".getBytes()
        );
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        new Hangman(input, output, Hangman.WORDS, 1).exec();
        assertThat(output.toString()).contains("You lost");
    }
}
