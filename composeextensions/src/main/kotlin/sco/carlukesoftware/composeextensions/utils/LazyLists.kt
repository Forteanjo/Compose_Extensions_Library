package sco.carlukesoftware.composeextensions.utils

import androidx.compose.foundation.lazy.LazyListState

/**
 * Checks if the LazyList is scrolled to the end.
 *
 * @return `true` if the last visible item is the last item in the list, `false` otherwise.
 */
fun LazyListState.isScrolledToEnd(): Boolean =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

/**
 * Checks if the lazy list is scrolled to the top.
 *
 * @return True if the first visible item is the first item in the list (index 0)
 *         and its scroll offset is 0, indicating it's fully visible at the top.
 *         False otherwise.
 */
fun LazyListState.isAtTop(): Boolean =
    firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0
