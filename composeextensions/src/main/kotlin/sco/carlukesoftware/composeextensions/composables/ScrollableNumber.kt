package sco.carlukesoftware.composeextensions.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

/**
 * A composable function that displays a scrollable list of numbers.
 * The user can scroll through the numbers, and the selected number will be highlighted.
 *
 * @param modifier The modifier to be applied to the composable.
 * @param initialValue The initial value to be displayed.
 * @param range The range of numbers to be displayed.
 * @param stepSize The step size between numbers.
 * @param onValueChange A callback that is invoked when the selected number changes.
 */
@Composable
fun ScrollableNumber(
    modifier: Modifier = Modifier,
    initialValue: Int = 50,
    range: IntRange = 0..99,
    stepSize: Int = 1,
    onValueChange: (Int) -> Unit,
) {
    val rangeCount = roundedRangeCount(range, stepSize)
    val initialIndex = (initialValue - range.first) / stepSize
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex
    )

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .matchParentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .width(70.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(70.dp)
            )

            HorizontalDivider(
                modifier = Modifier
                    .width(70.dp)
            )

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = rangeCount) { virtualIndex ->
                    val actualIndex = virtualIndex * stepSize
                    val number = actualIndex + range.first

                    val firstVisibleItem = remember {
                        derivedStateOf {
                            lazyListState.firstVisibleItemIndex
                        }
                    }

                    val isSelected = virtualIndex == firstVisibleItem.value + 1
                    Text(
                        text = number
                            .toString()
                            .padStart(
                                length = 2,
                                padChar = '0'
                            ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 8.dp
                            ),
                        textAlign = TextAlign
                            .Center,
                        style = MaterialTheme
                            .typography
                            .headlineSmall
                            .copy(
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onBackground
                                }
                            ),
                        fontSize = if (isSelected) 50.sp else 30.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.isScrollInProgress }
            .collect { isScrollInProgress ->
                if (isScrollInProgress) {
                    val firstVisibleItem = lazyListState.firstVisibleItemIndex
                    val actualIndex = firstVisibleItem * stepSize
                    val selectedNumber = actualIndex + range.first

                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(index = firstVisibleItem)
                    }

                    onValueChange(selectedNumber)
                }
            }
    }
}

fun roundedRangeCount(range: IntRange, stepSize: Int): Int =
     (range.count() + stepSize - 1) / stepSize

@Preview
@Composable
private fun ScrollableNumberPreview() {
    ScrollableNumber(
        modifier = Modifier
            .width(100.dp)
            .height(300.dp),
        onValueChange = { }
    )
}
