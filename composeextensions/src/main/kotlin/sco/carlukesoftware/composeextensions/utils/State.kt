package sco.carlukesoftware.composeextensions.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import sco.carlukesoftware.composeextensions.State

/**
 * Executes the given composable [block] if the [State] is [State.Idle].
 *
 * This is a convenience function to simplify conditional rendering based on the state.
 *
 * Example usage:
 * ```kotlin
 * val myState: State<String> = ...
 *
 * myState.onIdle {
 *     Text("Waiting for data...")
 * }
 * ```
 *
 * @param block The composable block to execute if the state is Idle.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <T> State<T>.onIdle(block: @Composable () -> Unit) {
    if (this is State.Idle) block()
}

/**
 * Executes the given composable [block] if the [State] is [State.Loading].
 *
 * This is a convenience function to simplify conditional rendering based on the state.
 *
 * Example usage:
 * ```kotlin
 * val myState: State<String> = ...
 *
 * myState.onLoading {
 *     CircularProgressIndicator()
 * }
 * ```
 *
 * @param block The composable block to execute if the state is Loading.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <T> State<T>.onLoading(block: @Composable () -> Unit) {
    if (this is State.Loading) block()
}

/**
 * Executes the given composable [block] if the [State] is [State.Success].
 * The [block] will receive the data from the [State.Success] as its parameter.
 *
 * This is a convenience function to simplify conditional rendering based on the state.
 *
 * Example usage:
 * ```kotlin
 * val myState: State<String> = ... // Assume this is State.Success("Hello")
 *
 * myState.onSuccess { data ->
 *     Text("Data loaded: $data")
 * }
 * ```
 *
 * @param block The composable block to execute if the state is Success. The data of the success state will be passed to this block.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <T> State<T>.onSuccess(block: @Composable (T) -> Unit) {
    if (this is State.Success) block(data)
}

/**
 * Executes the given composable [block] if the [State] is [State.Failure].
 * The [Throwable] associated with the failure is passed to the [block].
 *
 * This is a convenience function to simplify conditional rendering and error handling
 * based on the state.
 *
 * Example usage:
 * ```kotlin
 * val myState: State<String> = ...
 *
 * myState.onFailure { error ->
 *     Text("An error occurred: ${error.message}")
 * }
 * ```
 *
 * @param block The composable block to execute if the state is Failure.
 *              The [Throwable] that caused the failure is provided as an argument.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <T> State<T>.onFailure(block: @Composable (Throwable) -> Unit) {
    if (this is State.Failure) block(throwable)
}
