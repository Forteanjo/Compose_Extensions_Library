package sco.carlukesoftware.composeextensions.utils

import androidx.compose.runtime.Composable
import sco.carlukesoftware.composeextensions.UiState

/**
 * Exhaustively renders UI content based on the current [UiState].
 *
 * Example usage:
 * ```kotlin
 * myState.Content(
 *     onIdle = { Text("Waiting for input...") },
 *     onLoading = { CircularProgressIndicator() },
 *     onFailure = { error -> Text("Error: ${error.message}") },
 *     onSuccess = { data -> Text("Loaded: $data") }
 * )
 * ```
 */
@Composable
inline fun <T> UiState<T>.Content(
    crossinline onSuccess: @Composable (data: T) -> Unit,
    noinline onLoading: (@Composable () -> Unit)? = null,
    noinline onIdle: (@Composable () -> Unit)? = null,
    noinline onFailure: (@Composable (throwable: Throwable) -> Unit)? = null,
) {
    when (this) {
        is UiState.Idle -> onIdle?.invoke()
        is UiState.Loading -> onLoading?.invoke()
        is UiState.Failure -> onFailure?.invoke(throwable)
        is UiState.Success -> onSuccess(data)
    }
}

/**
 * Executes [block] if the [UiState] is [UiState.Idle].
 * Returns `this` to allow chaining.
 */
inline fun <T> UiState<T>.onIdle(block: () -> Unit): UiState<T> {
    if (this is UiState.Idle) block()
    return this
}

/**
 * Executes [block] if the [UiState] is [UiState.Loading].
 * Returns `this` to allow chaining.
 */
inline fun <T> UiState<T>.onLoading(block: () -> Unit): UiState<T> {
    if (this is UiState.Loading) block()
    return this
}

/**
 * Executes [block] with [UiState.Success.data] if the [UiState] is [UiState.Success].
 * Returns `this` to allow chaining.
 */
inline fun <T> UiState<T>.onSuccess(block: (data: T) -> Unit): UiState<T> {
    if (this is UiState.Success) block(data)
    return this
}

/**
 * Executes [block] with [UiState.Failure.throwable] if the [UiState] is [UiState.Failure].
 * Returns `this` to allow chaining.
 */
inline fun <T> UiState<T>.onFailure(block: (throwable: Throwable) -> Unit): UiState<T> {
    if (this is UiState.Failure) block(throwable)
    return this
}
