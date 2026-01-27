package sco.carlukesoftware.composeextensions

/**
 * A generic sealed class that represents the different states of an asynchronous operation or data loading process.
 * It is particularly useful for managing UI state in Jetpack Compose, allowing for exhaustive handling of all possible outcomes.
 *
 * @param T The type of data held by the [Success] state.
 */
sealed class State<out T> {
    data object Idle: State<Nothing>()
    data object Loading: State<Nothing>()
    data class Success<T>(val data: T): State<T>()
    data class Failure(val throwable: Throwable): State<Nothing>()
}
