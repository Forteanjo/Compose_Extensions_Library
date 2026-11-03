package sco.carlukesoftware.composeextensions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Creates and remembers a state that tracks whether the software keyboard (IME) is currently visible.
 *
 * This function observes the window insets for the IME and returns a [State] that
 * updates automatically when the keyboard is shown or hidden.
 *
 * @return A [State] object containing a boolean value that is `true` if the keyboard is visible, and `false` otherwise.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun rememberImeState(): State<Boolean> {
    val isImeVisible = WindowInsets.isImeVisible
    return rememberUpdatedState(isImeVisible)
}

/**
 * A container that automatically adjusts its padding to accommodate the software keyboard (IME)
 * and enables vertical scrolling.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param scrollState The [ScrollState] to manage vertical scrolling.
 * @param verticalArrangement The vertical arrangement of the layout's children.
 * @param horizontalAlignment The horizontal alignment of the layout's children.
 * @param enabled Controls the enabled state of the vertical scroll.
 * @param content The composable content inside the column container.
 */
@Composable
fun ImeScrollBox(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .imePadding()
            .verticalScroll(
                state = scrollState,
                enabled = enabled
            ),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}
