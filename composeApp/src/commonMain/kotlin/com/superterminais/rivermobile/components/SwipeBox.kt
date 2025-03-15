package com.superterminais.rivermobile.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.superterminais.rivermobile.ui.theme.SwipeLeftBackgroundColor
import com.superterminais.rivermobile.ui.theme.SwipeRightBackgroundColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBox(
    modifier: Modifier,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    enableSwipeToLeft: Boolean = true,
    enableSwipeToRight: Boolean = true,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
    val delay = 300L

    val backgroundColor = animateColorAsState(
        when (swipeToDismissBoxState.dismissDirection) {
            SwipeToDismissBoxValue.EndToStart -> SwipeLeftBackgroundColor
            SwipeToDismissBoxValue.StartToEnd -> SwipeRightBackgroundColor
            else -> Color.Gray
        }, label = "Color"
    )

    val backgroundIcon = when (swipeToDismissBoxState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Icons.Filled.Done
        SwipeToDismissBoxValue.StartToEnd -> Icons.Filled.Clear
        else -> null
    }

    val alignment = when (swipeToDismissBoxState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        else -> Alignment.Center
    }

    LaunchedEffect(swipeToDismissBoxState.currentValue) {
        when (swipeToDismissBoxState.currentValue) {
            SwipeToDismissBoxValue.StartToEnd -> {
                coroutineScope.launch {
                    onSwipeRight()
                    delay(delay)
                }
            }

            SwipeToDismissBoxValue.EndToStart -> {
                coroutineScope.launch {
                    onSwipeLeft()
                    delay(delay)
                }
            }

            SwipeToDismissBoxValue.Settled -> {}
        }
    }
    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        enableDismissFromEndToStart = enableSwipeToLeft,
        enableDismissFromStartToEnd = enableSwipeToRight,
        modifier = modifier,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor.value)
                    .padding(horizontal = 16.dp),
                contentAlignment = alignment
            ) {
                backgroundIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = Color.White
                    )
                }
            }
        }
    ) {
        content()
    }
}