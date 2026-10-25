package sco.carlukesoftware.composeextensions.windowinfo

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

/**
 * Holds information about the current window configuration and size classes.
 *
 * @property screenWidthInfo The horizontal window size class ([WindowType]).
 * @property screenHeightInfo The vertical window size class ([WindowType]).
 * @property screenWidth The screen width in density-independent pixels ([Dp]).
 * @property screenHeight The screen height in density-independent pixels ([Dp]).
 * @property orientation The current screen [Orientation].
 */
@Immutable
data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp,
    val orientation: Orientation
) {
    /**
     * Helper check if screen width is compact.
     */
    val isCompactWidth: Boolean get() = screenWidthInfo == WindowType.Compact

    /**
     * Helper check if orientation is portrait.
     */
    val isPortrait: Boolean get() = orientation == Orientation.Portrait

    /**
     * Helper check if orientation is landscape.
     */
    val isLandscape: Boolean get() = orientation == Orientation.Landscape

    /**
     * Represents the breakpoint size class of the window dimensions.
     * Enums automatically implement [Comparable] ordered by declaration (Compact < Medium < Expanded).
     */
    enum class WindowType {
        Compact,
        Medium,
        Expanded
    }
}
