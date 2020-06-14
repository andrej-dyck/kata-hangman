package ad.kata.hangman

import ad.kata.hangman.oo.Word
import ad.kata.hangman.oo.WordList
import ad.kata.hangman.oo.Hangman as OoHangman
import ad.kata.hangman.procedural.Hangman as ProceduralHangman

fun main() {
    val words = ProceduralHangman.WORDS

    if (RELEASE_TOGGLE_OO_VERSION) {
        OoHangman(
            WordList(words?.map { Word(it) } ?: emptyList()),
            System.`in`,
            System.out
        ).exec()
    } else {
        ProceduralHangman(
            System.`in`,
            System.out,
            words,
            5
        ).exec()
    }
}

var RELEASE_TOGGLE_OO_VERSION = true
