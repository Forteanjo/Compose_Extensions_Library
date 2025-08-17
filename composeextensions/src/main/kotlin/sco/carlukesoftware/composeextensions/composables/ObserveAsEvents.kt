package sco.carlukesoftware.composeextensions.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Observes a [Flow] and executes the [onEvent] callback for each emitted value.
 *
 * This composable function is designed to handle events from a [Flow] in a lifecycle-aware manner.
 * It uses [LaunchedEffect] and [repeatOnLifecycle] to ensure that the flow is collected only when
 * the composable is in the STARTED state and is automatically cancelled when the composable is destroyed.
 *
 * This is particularly useful for scenarios where you want to trigger side effects (e.g., showing a Snackbar,
 * navigating to another screen) based on events emitted by a ViewModel or another business logic component.
 *
 * @param T The type of data emitted by the [Flow].
 * @param flow The [Flow] to observe.
 * @param key1 An optional key that, if changed, will cause the [LaunchedEffect] to be cancelled and restarted.
 *             This can be useful if the observation logic depends on this key.
 * @param key2 Another optional key that, if changed, will cause the [LaunchedEffect] to be cancelled and restarted.
 * @param onEvent A lambda function that will be invoked with each value emitted by the [flow].
 *                This function is executed on the main thread.
 */
@Composable
fun <T>ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(
        lifecycleOwner.lifecycle, key1, key2, flow
    ) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}
