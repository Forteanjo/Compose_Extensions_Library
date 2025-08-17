package sco.carlukesoftware.composeextensions.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

/**
 * Closes the virtual keyboard.
 *
 * This Composable function provides a way to programmatically close the soft keyboard.
 *
 * @param keepFocus If `true`, the keyboard will be hidden but the current focus will be
 * maintained. If `false`, the keyboard will be hidden and the focus will be cleared from
 * the currently focused element.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CloseVirtualKeyboard(keepFocus: Boolean) {
    if (keepFocus) {
        val keyboardController = LocalSoftwareKeyboardController.current
        keyboardController?.hide()
    } else {
        val focusManager = LocalFocusManager.current
        focusManager.clearFocus()
    }
}
