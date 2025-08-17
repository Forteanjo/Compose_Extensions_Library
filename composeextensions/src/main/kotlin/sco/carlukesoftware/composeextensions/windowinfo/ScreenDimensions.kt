package sco.carlukesoftware.composeextensions.windowinfo

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalResources

/**
 * Composable function to get the current screen height in Dp.
 *
 * This function accesses the display metrics from the `LocalContext`
 * and converts the height from pixels to Dp using the `LocalDensity`.
 *
 * @return The current screen height as a [Dp] value.
 */
@Composable
fun screenHeight(): Dp {
    return LocalResources.current.displayMetrics.heightPixels.dp /
            LocalDensity.current.density
}

/**
 * Returns the width of the screen in Dp.
 *
 * This Composable function calculates the screen width by accessing the display metrics
 * from the current `LocalContext` and converting the pixel width to Dp using the
 * current `LocalDensity`.
 *
 * @return The screen width as a [Dp] value.
 */
@Composable
fun screenWidth(): Dp {
    return LocalResources.current.displayMetrics.widthPixels.dp /
            LocalDensity.current.density
}
