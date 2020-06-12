package ad.kata.hangman

import ad.kata.hangman.oo.Word
import ad.kata.hangman.oo.WordList
import ad.kata.hangman.oo.Hangman as OoHangman
import ad.kata.hangman.procedural.Hangman as ProceduralHangman

fun main() {
    if (RELEASE_TOGGLE_OO_VERSION) {
        OoHangman(
            listOfWords(ProceduralHangman.WORDS),
            System.`in`,
            System.out
        ).exec()
    } else {
        ProceduralHangman(
            System.`in`,
            System.out,
            ProceduralHangman.WORDS,
            5
        ).exec()
    }
}

fun listOfWords(words: Array<String>?) =
    WordList(words?.map { Word(it) } ?: emptyList())

var RELEASE_TOGGLE_OO_VERSION = true
