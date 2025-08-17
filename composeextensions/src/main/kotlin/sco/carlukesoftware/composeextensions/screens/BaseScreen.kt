package sco.carlukesoftware.composeextensions.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import sco.carlukesoftware.composeextensions.lifecycle.ListenToComposableLifecycle

/**
 * Base class for creating screens in a Compose-based application.
 *
 * This class provides a basic structure for screens, including lifecycle event handling.
 * It uses [ListenToComposableLifecycle] to observe lifecycle events and calls corresponding
 * methods like `onResume`, `onPause`, `onStop`, and `onDestroy`.
 *
 * Subclasses must implement the [OnScreenCreated] method to define the UI content of the screen.
 * They can also override the lifecycle methods to perform specific actions at different stages
 * of the screen's lifecycle.
 */
abstract class BaseScreen {

    /**
     * Creates the screen and listens to lifecycle events.
     *
     * This function is responsible for setting up the screen's lifecycle listeners
     * and then calling [OnScreenCreated] to render the actual UI of the screen.
     *
     * It uses [ListenToComposableLifecycle] to observe lifecycle events (ON_RESUME,
     * ON_PAUSE, ON_STOP, ON_DESTROY) and delegates them to the corresponding
     * `onXXX` methods ([onResume], [onPause], [onStop], [onDestroy]).
     *
     * @param modifier The modifier to be applied to the screen's layout.
     */
    @Composable
    open fun CreateScreen(
        modifier: Modifier
    ) {
        ListenToComposableLifecycle(
            onEvent = { lifecycleOwner, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> onResume(lifecycleOwner)
                    Lifecycle.Event.ON_PAUSE -> onPause(lifecycleOwner)
                    Lifecycle.Event.ON_STOP -> onStop(lifecycleOwner)
                    Lifecycle.Event.ON_DESTROY -> onDestroy(lifecycleOwner)
                    else -> { }
                }
            }
        )

        OnScreenCreated(modifier = modifier)
    }



    @Composable
    protected abstract fun OnScreenCreated(modifier: Modifier)
    protected open fun onResume(lifecycleOwner: LifecycleOwner) { }
    protected open fun onPause(lifecycleOwner: LifecycleOwner) { }
    protected open fun onStop(lifecycleOwner: LifecycleOwner) { }
    protected open fun onDestroy(lifecycleOwner: LifecycleOwner) { }



}
