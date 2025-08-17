package sco.carlukesoftware.composeextensions

sealed class State<out T> {
    data object Idle: State<Nothing>()
    data object Loading: State<Nothing>()
    data class Success<T>(val data: T): State<T>()
    data class Failure(val throwable: Throwable): State<Nothing>()
}
