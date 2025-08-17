package sco.carlukesoftware.composeextensions.snackbars.data

import androidx.compose.material3.SnackbarDuration

/**
 * Represents an event that triggers the display of a Snackbar.
 *
 * @param message The text message to be displayed in the Snackbar.
 * @param action An optional [SnackbarAction] that defines an action button for the Snackbar.
 *               Defaults to `null`, meaning no action button will be shown.
 * @param duration The duration for which the Snackbar should be displayed.
 *                 Defaults to [SnackbarDuration.Long].
 */
data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null,
    val duration: SnackbarDuration = SnackbarDuration.Long
)
