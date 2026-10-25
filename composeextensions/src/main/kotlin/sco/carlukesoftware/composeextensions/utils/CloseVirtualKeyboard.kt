package sco.carlukesoftware.composeextensions.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

/**
 * A Composable side effect that closes the virtual keyboard when entering the composition.
 *
 * @param keepFocus If `true`, the keyboard will be hidden while keeping focus on the current element.
 * If `false` (default), focus will also be cleared.
 */
@Composable
fun CloseKeyboardEffect(keepFocus: Boolean = false) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(keepFocus) {
        if (keepFocus) {
            keyboardController?.hide()
        } else {
            focusManager.clearFocus()
        }
    }
}
