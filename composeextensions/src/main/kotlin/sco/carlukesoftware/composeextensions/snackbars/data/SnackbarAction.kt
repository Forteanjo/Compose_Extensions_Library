package sco.carlukesoftware.composeextensions.snackbars.data

/**
 * Data class representing an action that can be performed from a Snackbar.
 *
 * @property name The text to display for the action button.
 * @property action The lambda function to be executed when the action button is clicked.
 */
data class SnackbarAction(
    val name: String,
    val action: () -> Unit
)
