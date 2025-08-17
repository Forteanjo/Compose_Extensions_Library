package sco.carlukesoftware.composeextensions.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Observes the lifecycle of a Composable function.
 *
 * This function allows you to execute specific actions in response to lifecycle events
 * of a Composable, such as when it's created, started, resumed, paused, stopped, or destroyed.
 *
 * It uses a `DisposableEffect` to ensure that the lifecycle observer is properly added when the
 * Composable enters the composition and removed when it leaves, preventing memory leaks.
 *
 * @param lifecycleOwner The [LifecycleOwner] whose lifecycle events are to be observed.
 *                       Defaults to the [LocalLifecycleOwner] of the current composition.
 *                       This allows you to observe the lifecycle of the Composable itself or
 *                       any other [LifecycleOwner] available in the scope.
 * @param onEvent A lambda function that will be invoked when a lifecycle event occurs.
 *                It receives two parameters:
 *                - `source`: The [LifecycleOwner] that triggered the event.
 *                - `event`: The [Lifecycle.Event] that occurred (e.g., ON_CREATE, ON_START).
 *
 * Example usage:
 * ```kotlin
 * ListenToComposableLifecycle { source, event ->
 *     when (event) {
 *         Lifecycle.Event.ON_START -> {
 *             // Composable has started
 *         }
 *         Lifecycle.Event.ON_STOP -> {
 *             // Composable has stopped
 *         }
 */
@Composable
fun ListenToComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
