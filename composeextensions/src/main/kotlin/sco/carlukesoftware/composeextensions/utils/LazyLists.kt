package sco.carlukesoftware.composeextensions.utils

import androidx.compose.foundation.lazy.LazyListState

/**
 * Checks if the first item in the lazy list is visible at index 0 and fully aligned at the top (scroll offset 0).
 *
 * **Performance Note:** When reading this property inside a Composable, wrap it in [androidx.compose.runtime.derivedStateOf]
 * to prevent recomposition on every pixel scrolled:
 * ```
 * val isAtTop by remember { derivedStateOf { listState.isAtTop } }
 * ```
 */
val LazyListState.isAtTop: Boolean
    get() = firstVisibleItemIndex == 0 && firstVisibleItemScrollOffset == 0

/**
 * Checks if the last item in the lazy list is currently visible in the viewport (at least partially).
 *
 * Useful as a trigger for pagination or auto-loading more data.
 *
 * **Note:** This returns `true` as soon as any part of the last item becomes visible. If you need to check
 * if the list cannot be scrolled forward any further, use `!canScrollForward` instead.
 */
val LazyListState.isLastItemVisible: Boolean
    get() {
        val totalItems = layoutInfo.totalItemsCount
        if (totalItems == 0) return false
        return layoutInfo.visibleItemsInfo.lastOrNull()?.index == totalItems - 1
    }

/**
 * Checks if the lazy list is scrolled completely to the end (no remaining forward scroll distance).
 *
 * Uses Compose's built-in [LazyListState.canScrollForward] property.
 */
val LazyListState.isScrolledToEnd: Boolean
    get() = !canScrollForward
