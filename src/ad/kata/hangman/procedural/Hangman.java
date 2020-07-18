package ad.kata.hangman.procedural;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Hangman {

    public static final String[] WORDS = {
            "simplicity", "equality", "grandmother",
            "neighborhood", "relationship", "mathematics",
            "university", "explanation"
    };

    private final InputStream input;
    private final OutputStream output;
    private final String[] dictionary;
    private final int max;

    public Hangman(final InputStream in, final OutputStream out, final String[] words, final int m) {
        this.input = in;
        this.output = out;
        this.dictionary = words; /* REQUIRED FOR REGRESSION TESTS */
        this.max = m;
    }

    public void exec() {
        String word = dictionary[new Random().nextInt(dictionary.length)];
        boolean[] visible = new boolean[word.length()];
        int mistakes = 0;
        try (final PrintStream out = new PrintStream(this.output)) {
            final Iterator<String> scanner = new Scanner(this.input);
            boolean done = true;
            while (mistakes < this.max) {
                done = true;
                for (int i = 0; i < word.length(); ++i) {
                    if (!visible[i]) {
                        done = false;
                    }
                }
                if (done) {
                    break;
                }
                out.print("Guess a letter: ");
                char chr = scanner.next().charAt(0);
                boolean hit = false;
                for (int i = 0; i < word.length(); ++i) {
                    if (word.charAt(i) == chr && !visible[i]) {
                        visible[i] = true;
                        hit = true;
                    }
                }
                if (hit) {
                    out.print("Hit!\n");
                } else {
                    out.printf(
                        "Missed, mistake #%d out of %d\n",
                        mistakes + 1, this.max
                    );
                    ++mistakes;
                }
                out.append("The word: ");
                for (int i = 0; i < word.length(); ++i) {
                    if (visible[i]) {
                        out.print(word.charAt(i));
                    } else {
                        out.print("?");
                    }
                }
                out.append("\n\n");
            }
            if (done) {
                out.print("You won!\n");
            } else {
                out.print("You lost.\n");
            }
        } catch (Exception e) {
            /* REQUIRED FOR REGRESSION TESTS */
        }
    }

}
