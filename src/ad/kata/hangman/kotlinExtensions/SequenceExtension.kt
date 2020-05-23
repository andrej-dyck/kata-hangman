package ad.kata.hangman.kotlinExtensions

fun <T> Sequence<T>.takeWithFirst(predicate: (T) -> Boolean) =
    sequence {
        for (element in this@takeWithFirst) {
            yield(element)

            if (predicate(element)) return@sequence
        }
    }