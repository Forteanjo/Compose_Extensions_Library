package sco.carlukesoftware.composeextensions.windowinfo

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun rememberWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    val screenWidth by remember(key1 = configuration) {
        mutableIntStateOf(configuration.screenWidthDp)
    }
    val screenHeight by remember(key1 = configuration) {
        mutableIntStateOf(configuration.screenHeightDp)
    }

    val orientation by remember(key1 = configuration) {
        mutableIntStateOf(configuration.orientation)
    }

    return WindowInfo(
        screenWidthInfo = getScreenWidth(screenWidth),
        screenHeightInfo = getScreenHeight(screenHeight),
        screenWidth = screenWidth.dp,
        screenHeight = screenHeight.dp,
        orientation = getOrientation(orientation)
    )
}

fun getScreenWidth(screenWidth: Int): WindowInfo.WindowType = when {
    screenWidth < 600 -> WindowInfo.WindowType.Compact
    screenWidth < 840 -> WindowInfo.WindowType.Medium
    else -> WindowInfo.WindowType.Expanded
}

fun getScreenHeight(screenHeight: Int): WindowInfo.WindowType = when {
    screenHeight < 480 -> WindowInfo.WindowType.Compact
    screenHeight < 900 -> WindowInfo.WindowType.Medium
    else -> WindowInfo.WindowType.Expanded
}

fun getOrientation(orientation: Int): Orientation = when {
    orientation == ORIENTATION_PORTRAIT -> Orientation.Portrait
    else -> Orientation.Landscape
}
