package sco.carlukesoftware.composeextensions.snackbars

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import sco.carlukesoftware.composeextensions.snackbars.data.SnackbarEvent

/**
 * A controller for managing snackbar events.
 *
 * This object provides a centralized way to send and receive snackbar events,
 * allowing different parts of the application to trigger snackbar notifications.
 *
 * Snackbars are displayed using a [SnackbarHostState] which should collect the [events] flow.
 *
 * Example usage:
 * ```kotlin
 * // In your Composable
 * val snackbarHostState = remember { SnackbarHostState() }
 * LaunchedEffect(Unit) {
 *     SnackbarController.events.collect { event ->
 *         snackbarHostState.showSnackbar(
 *             message = event.message,
 *             actionLabel = event.actionLabel,
 *             duration = event.duration
 *         )
 *     }
 * }
 *
 * // To show a snackbar from anywhere
 * CoroutineScope(Dispatchers.Main).launch {
 *     SnackbarController.sendEvent(
 *         SnackbarEvent(message = "Hello Snackbar!")
 *     )
 * }
 * ```
 */
object SnackbarController {

    private val _events = Channel<SnackbarEvent>()
    val events = _events.receiveAsFlow()

    /**
     * Sends a [SnackbarEvent] to be displayed.
     * This function is suspending and should be called from a coroutine.
     *
     * @param event The [SnackbarEvent] to send.
     */
    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(element = event)
    }
}
