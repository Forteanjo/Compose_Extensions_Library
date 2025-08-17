package sco.carlukesoftware.composeextensions.windowinfo

import androidx.compose.ui.unit.Dp

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp,
    val orientation: Orientation
) {

    sealed class WindowType {
        data object Compact: WindowType()
        data object Medium: WindowType()
        data object Expanded: WindowType()

        operator fun compareTo(other: WindowType): Int {
            return if (this === other) 0 else {
                if (this == Compact) {
                    if (other == Medium || other == Expanded) -1
                    else 1
                } else if (this == Medium) {
                    if (other == Expanded) -1 else 1
                } else 1
            }
        }
    }
}
