package sco.carlukesoftware.composeextensions.windowinfo

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The current screen height in [Dp] based on configuration bounds.
 */
val screenHeight: Dp
    @Composable
    get() = LocalConfiguration.current.screenHeightDp.dp

/**
 * The current screen width in [Dp] based on configuration bounds.
 */
val screenWidth: Dp
    @Composable
    get() = LocalConfiguration.current.screenWidthDp.dp
