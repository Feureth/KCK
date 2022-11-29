package pl.feureth.view.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.coerceIn
import androidx.compose.ui.unit.dp

@Composable
fun ScrollBar(scrollState: LazyListState, modifier: Modifier = Modifier) {

    if (scrollState.layoutInfo.totalItemsCount == 0) {
        return
    }

    val density = LocalDensity.current
    val firstVisibleItem = scrollState.firstVisibleItemIndex
    val visibleItems = scrollState.layoutInfo.visibleItemsInfo.size
    val itemsCount = scrollState.layoutInfo.totalItemsCount
    var viewHeightPx by remember { mutableStateOf(0) }

    val viewHeight by remember(viewHeightPx) {
        derivedStateOf {
            with(density) { viewHeightPx.toDp() }
        }
    }
    val barHeight by remember(viewHeight, visibleItems, itemsCount) {
        derivedStateOf {
            viewHeight * (visibleItems / itemsCount.toFloat())
        }
    }

    var isDragging by remember { mutableStateOf(false) }
    var manualBarPosition by remember { mutableStateOf(0.dp) }
    val barPosition by derivedStateOf {
        if (isDragging) {
            manualBarPosition
        } else {
            viewHeight * (firstVisibleItem / itemsCount.toFloat())
        }
    }

    Box(
        modifier = Modifier.width(8.dp).fillMaxHeight().background(Color.LightGray)
            .then(modifier)
            .onSizeChanged {
                viewHeightPx = it.height
            }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(barHeight).offset(y = barPosition)
                .background(MaterialTheme.colors.primary, RoundedCornerShape(.5f))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            manualBarPosition = barPosition
                            isDragging = true
                        },
                        onDrag = { change, dragAmount ->
                            manualBarPosition =
                                (manualBarPosition + dragAmount.y.toDp()).coerceIn(0.dp, viewHeight - barHeight)
                        },
                        onDragEnd = {
                            isDragging = false
                        }
                    )
                }
        )
    }
}