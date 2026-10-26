package sco.carlukesoftware.composeextensions.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Observes a [Flow] and executes the [onEvent] callback for each emitted value in a lifecycle-aware manner.
 *
 * Uses [LaunchedEffect] and [repeatOnLifecycle] to ensure flow collection occurs only when
 * the composable's lifecycle is at least in [minActiveState] (defaults to [Lifecycle.State.STARTED]).
 *
 * Employs [rememberUpdatedState] so that [onEvent] always invokes the most recent lambda instance
 * across recompositions without restarting the underlying flow collection.
 *
 * @param T The type of data emitted by the [Flow].
 * @param flow The [Flow] to observe.
 * @param key1 An optional key that, if changed, will restart flow collection.
 * @param key2 Another optional key that, if changed, will restart flow collection.
 * @param minActiveState The minimum [Lifecycle.State] required to collect events. Defaults to [Lifecycle.State.STARTED].
 * @param onEvent Callback invoked for each value emitted by [flow].
 */
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnEvent by rememberUpdatedState(onEvent)

    LaunchedEffect(lifecycleOwner.lifecycle, flow, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(minActiveState) {
            flow.collect { event ->
                currentOnEvent(event)
            }
        }
    }
}

/**
 * Extension function variant of [ObserveAsEvents] for convenient fluent syntax.
 *
 * Example:
 * ```kotlin
 * viewModel.uiEvent.ObserveAsEvents { event ->
 *     when (event) { ... }
 * }
 * ```
 */
@Composable
@JvmName("ObserveAsEventsFromFlow")
fun <T> Flow<T>.ObserveAsEvents(
    key1: Any? = null,
    key2: Any? = null,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    onEvent: (T) -> Unit
) {
    ObserveAsEvents(
        flow = this,
        key1 = key1,
        key2 = key2,
        minActiveState = minActiveState,
        onEvent = onEvent
    )
}
