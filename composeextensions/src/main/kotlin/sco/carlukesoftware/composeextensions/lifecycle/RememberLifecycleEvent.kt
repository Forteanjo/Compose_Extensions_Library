package sco.carlukesoftware.composeextensions.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Remembers the current lifecycle event and updates it as the lifecycle changes.
 *
 * This Composable function observes the lifecycle of the provided [lifecycleOwner]
 * (defaulting to the [LocalLifecycleOwner]) and returns the latest [Lifecycle.Event].
 * The returned event will trigger recomposition when it changes.
 *
 * This is useful for performing actions or updating UI based on the current lifecycle state
 * of a component (e.g., an Activity or Fragment).
 *
 * The observer is automatically added when the Composable enters the composition and
 * removed when it leaves, preventing memory leaks.
 *
 * @param lifecycleOwner The [LifecycleOwner] whose lifecycle events are to be observed.
 * Defaults to [LocalLifecycleOwner.current].
 * @return The current [Lifecycle.Event]. Initially [Lifecycle.Event.ON_ANY], then updates
 * as lifecycle events occur.
 */
@Composable
fun rememberLifecycleEvent(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
): Lifecycle.Event  {
    var state by remember {
        mutableStateOf(Lifecycle.Event.ON_ANY)
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            state  = event
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    return state
}

@Composable
fun RememberTest(modifier: Modifier = Modifier) {
    val lifecycleEvent = rememberLifecycleEvent()
    LaunchedEffect(key1 = lifecycleEvent) {
        println("debug lifecycle event is ${lifecycleEvent}")
        if(lifecycleEvent == Lifecycle.Event.ON_RESUME) {

        }
    }
}
