package ad.kata.hangman

import ad.kata.hangman.procedural.Hangman as ProceduralHangman

fun main() {
    ProceduralHangman(System.`in`, System.out, 5).exec()
}