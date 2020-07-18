package ad.kata.hangman.kotlinExtensions

fun <T> Sequence<T>.takeWithFirst(predicate: (T) -> Boolean) =
    sequence {
        for (element in this@takeWithFirst) {
            yield(element)

            if (predicate(element)) return@sequence
        }
    }

@OptIn(ExperimentalStdlibApi::class)
fun <T, R> Sequence<T>.runningFold(initial: R, operation: (acc: R, T) -> R): Sequence<R> =
    scan(initial, operation)
