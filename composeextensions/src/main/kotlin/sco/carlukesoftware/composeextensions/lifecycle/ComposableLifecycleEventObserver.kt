package sco.carlukesoftware.composeextensions.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * A Composable function that observes lifecycle events and invokes the corresponding methods
 * on the provided [ComposableLifecycleObserver].
 *
 * Automatically tracks updates to [observer] to ensure callbacks always execute on the current instance.
 *
 * @param observer An instance of [ComposableLifecycleObserver] that receives lifecycle event callbacks.
 * @param lifecycleOwner The [LifecycleOwner] to observe. Defaults to [LocalLifecycleOwner.current].
 */
@Composable
fun ComposableLifecycleEventObserver(
    observer: ComposableLifecycleObserver,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val currentObserver by rememberUpdatedState(observer)

    ListenToComposableLifecycle(lifecycleOwner = lifecycleOwner) { owner, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> currentObserver.onCreate(owner)
            Lifecycle.Event.ON_START -> currentObserver.onStart(owner)
            Lifecycle.Event.ON_RESUME -> currentObserver.onResume(owner)
            Lifecycle.Event.ON_PAUSE -> currentObserver.onPause(owner)
            Lifecycle.Event.ON_STOP -> currentObserver.onStop(owner)
            Lifecycle.Event.ON_DESTROY -> currentObserver.onDestroy(owner)
            Lifecycle.Event.ON_ANY -> { /* Handled via onAny below */ }
        }

        currentObserver.onAny(owner, event)
    }
}

/**
 * Convenience Composable function to observe specific lifecycle events via individual callbacks.
 *
 * Example usage:
 * ```kotlin
 * ComposableLifecycleEventObserver(
 *     onResume = { /* React to resume */ },
 *     onPause = { /* React to pause */ },
 * )
 * ```
 */
@Composable
fun ComposableLifecycleEventObserver(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onCreate: ((LifecycleOwner) -> Unit)? = null,
    onStart: ((LifecycleOwner) -> Unit)? = null,
    onResume: ((LifecycleOwner) -> Unit)? = null,
    onPause: ((LifecycleOwner) -> Unit)? = null,
    onStop: ((LifecycleOwner) -> Unit)? = null,
    onDestroy: ((LifecycleOwner) -> Unit)? = null,
    onAny: ((LifecycleOwner, Lifecycle.Event) -> Unit)? = null,
) {
    ComposableLifecycleEventObserver(
        lifecycleOwner = lifecycleOwner,
        observer = object : ComposableLifecycleObserver {
            override fun onCreate(lifecycleOwner: LifecycleOwner) { onCreate?.invoke(lifecycleOwner) }
            override fun onStart(lifecycleOwner: LifecycleOwner) { onStart?.invoke(lifecycleOwner) }
            override fun onResume(lifecycleOwner: LifecycleOwner) { onResume?.invoke(lifecycleOwner) }
            override fun onPause(lifecycleOwner: LifecycleOwner) { onPause?.invoke(lifecycleOwner) }
            override fun onStop(lifecycleOwner: LifecycleOwner) { onStop?.invoke(lifecycleOwner) }
            override fun onDestroy(lifecycleOwner: LifecycleOwner) { onDestroy?.invoke(lifecycleOwner) }
            override fun onAny(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
                onAny?.invoke(lifecycleOwner, event)
            }
        }
    )
}
