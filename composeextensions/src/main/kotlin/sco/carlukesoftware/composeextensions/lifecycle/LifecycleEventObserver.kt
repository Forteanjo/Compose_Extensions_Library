package sco.carlukesoftware.composeextensions.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle

/**
 * A Composable function that observes lifecycle events and invokes the corresponding methods
 * on the provided [ILifecycleEventObserver].
 *
 * This function allows you to react to lifecycle changes of the Composable it's placed in.
 *
 * @param observer An instance of [ILifecycleEventObserver] that will receive lifecycle event callbacks.
 */
@Composable
fun LifecycleEventObserver(
    observer: ILifecycleEventObserver
) {
    ListenToComposableLifecycle { lifecycleOwner, event ->
        when(event) {
            Lifecycle.Event.ON_CREATE -> observer.onCreate(lifecycleOwner = lifecycleOwner)
            Lifecycle.Event.ON_DESTROY -> observer.onDestroy(lifecycleOwner = lifecycleOwner)
            Lifecycle.Event.ON_PAUSE -> observer.onPause(lifecycleOwner = lifecycleOwner)
            Lifecycle.Event.ON_RESUME -> observer.onResume(lifecycleOwner = lifecycleOwner)
            Lifecycle.Event.ON_START -> observer.onStart(lifecycleOwner = lifecycleOwner)
            Lifecycle.Event.ON_STOP -> observer.onStop(lifecycleOwner = lifecycleOwner)
            else -> { }
        }

        observer.onAny(lifecycleOwner = lifecycleOwner)
    }
}
