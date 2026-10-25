package sco.carlukesoftware.composeextensions.windowinfo

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

// Material Design Window Size Class Breakpoints
private const val COMPACT_WIDTH_DP = 600
private const val MEDIUM_WIDTH_DP = 840

private const val COMPACT_HEIGHT_DP = 480
private const val MEDIUM_HEIGHT_DP = 900

/**
 * Remembers and calculates the current [WindowInfo] based on screen configuration.
 *
 * Automatically recalculates when width, height, or orientation changes.
 *
 * @return The current [WindowInfo] details.
 */
@Composable
fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current

    return remember(
        configuration.screenWidthDp,
        configuration.screenHeightDp,
        configuration.orientation
    ) {
        WindowInfo(
            screenWidthInfo = getScreenWidth(configuration.screenWidthDp),
            screenHeightInfo = getScreenHeight(configuration.screenHeightDp),
            screenWidth = configuration.screenWidthDp.dp,
            screenHeight = configuration.screenHeightDp.dp,
            orientation = getOrientation(configuration.orientation)
        )
    }
}

/**
 * Maps screen width in dp to [WindowInfo.WindowType].
 */
internal fun getScreenWidth(screenWidth: Int): WindowInfo.WindowType = when {
    screenWidth < COMPACT_WIDTH_DP -> WindowInfo.WindowType.Compact
    screenWidth < MEDIUM_WIDTH_DP -> WindowInfo.WindowType.Medium
    else -> WindowInfo.WindowType.Expanded
}

/**
 * Maps screen height in dp to [WindowInfo.WindowType].
 */
internal fun getScreenHeight(screenHeight: Int): WindowInfo.WindowType = when {
    screenHeight < COMPACT_HEIGHT_DP -> WindowInfo.WindowType.Compact
    screenHeight < MEDIUM_HEIGHT_DP -> WindowInfo.WindowType.Medium
    else -> WindowInfo.WindowType.Expanded
}

/**
 * Maps Android orientation integer to [Orientation].
 */
internal fun getOrientation(orientation: Int): Orientation = when (orientation) {
    ORIENTATION_PORTRAIT -> Orientation.Portrait
    else -> Orientation.Landscape
}
