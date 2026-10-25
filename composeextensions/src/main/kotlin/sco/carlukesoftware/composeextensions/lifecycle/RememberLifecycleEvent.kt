package sco.carlukesoftware.composeextensions.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Remembers the current lifecycle event and updates it as the lifecycle changes.
 *
 * This Composable function observes the lifecycle of the provided [lifecycleOwner]
 * (defaulting to [LocalLifecycleOwner]) and returns the latest [Lifecycle.Event].
 * The returned event will trigger recomposition when it changes.
 *
 * This is useful for performing actions or updating UI based on the current lifecycle state
 * of a component (e.g., an Activity or Fragment).
 *
 * The observer is automatically added when the Composable enters the composition and
 * removed when it leaves, preventing memory leaks.
 *
 * @param lifecycleOwner The [LifecycleOwner] whose lifecycle events are to be observed.
 * Defaults to [LocalLifecycleOwner].
 * @return The current [Lifecycle.Event]. Initially derived from the current lifecycle state
 * (or [Lifecycle.Event.ON_CREATE]), then updates as lifecycle events occur.
 */
@Composable
fun rememberLifecycleEvent(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
): Lifecycle.Event {
    var state by remember(lifecycleOwner) {
        mutableStateOf(
            Lifecycle.Event.upTo(lifecycleOwner.lifecycle.currentState) ?: Lifecycle.Event.ON_CREATE
        )
    }

    ListenToComposableLifecycle(lifecycleOwner = lifecycleOwner) { event ->
        state = event
    }

    return state
}
