package sco.carlukesoftware.composeextensions.snackbars

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import sco.carlukesoftware.composeextensions.snackbars.data.SnackbarAction
import sco.carlukesoftware.composeextensions.snackbars.data.SnackbarEvent

/**
 * A controller for managing snackbar events.
 *
 * Provides a centralized way to send and receive snackbar events across different
 * parts of the application.
 *
 * Note: [events] uses a single-consumer channel flow. Ensure only one `SnackbarHost`
 * collects events at the root of your UI hierarchy.
 *
 * Example usage:
 * ```kotlin
 * // In your root Composable (e.g., inside a Scaffold)
 * val snackbarHostState = remember { SnackbarHostState() }

 * SnackbarController.events.ObserveAsEvents { event ->
 *     val result = snackbarHostState.showSnackbar(
 *         message = event.message,
 *         actionLabel = event.action?.name,
 *         duration = event.duration
 *     )
 *     if (result == SnackbarResult.ActionPerformed) {
 *         event.action?.action?.invoke()
 *     }
 * }
 *
 * // Triggering a snackbar from a ViewModel or CoroutineScope
 * viewModelScope.launch {
 *     SnackbarController.sendEvent("Item saved successfully")
 * }
 * ```
 */
object SnackbarController {

    private val _events = Channel<SnackbarEvent>(capacity = Channel.BUFFERED)
    val events: Flow<SnackbarEvent> = _events.receiveAsFlow()

    /**
     * Sends a [SnackbarEvent] to be displayed.
     *
     * @param event The [SnackbarEvent] to send.
     */
    suspend fun sendEvent(event: SnackbarEvent) {
        _events.send(event)
    }

    /**
     * Convenience function to send a [SnackbarEvent] without constructing a data class instance.
     */
    suspend fun sendEvent(
        message: String,
        action: SnackbarAction? = null,
        duration: SnackbarDuration = SnackbarDuration.Long
    ) {
        sendEvent(SnackbarEvent(message, action, duration))
    }

    /**
     * Attempts to send a [SnackbarEvent] synchronously without suspending.
     *
     * @param event The [SnackbarEvent] to send.
     * @return A [ChannelResult] indicating whether the event was successfully sent.
     */
    fun trySendEvent(event: SnackbarEvent): ChannelResult<Unit> {
        return _events.trySend(event)
    }
}
