package ad.kata.hangman

import ad.kata.hangman.procedural.Hangman as ProceduralHangman

fun main() {
    ProceduralHangman(System.`in`, System.out, ProceduralHangman.WORDS, 5).exec()
}