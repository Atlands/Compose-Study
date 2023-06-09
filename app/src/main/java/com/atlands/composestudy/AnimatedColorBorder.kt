package com.atlands.composestudy

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedColorBorder(
    radius: Dp = 0.dp,
    width: Dp = 1.dp,
    content: @Composable () -> Unit
) {
    LaunchedEffect(Unit){
        print("width:::: ${width.value}")
    }
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box {
        Box(
            Modifier
                .clip(customShape(radius, width))
                .padding()
                .matchParentSize()
                .scale(10f)
                .rotate(angle)
                .background(
                    brush = Brush.sweepGradient(
                        listOf(
                            Color(255, 0, 0),
                            Color(0, 255, 0),
                            Color(139, 0, 255),
                            Color(255, 0, 0),
                        )
                    )
                )
        ) {}
        Box(
            modifier = Modifier
                .padding(width)
                .clip(RoundedCornerShape(radius))
        ) {
            content()
        }
    }
}

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

@Composable
private fun customShape(radius: Dp, width: Dp): GenericShape {
    val pxRadius = radius.toPx()
    val pxWidth = width.toPx()
    return GenericShape { size, _ ->
        op(path1 = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = size.toRect(),
                    cornerRadius = CornerRadius(pxRadius, pxRadius)
                )
            )
        }, path2 = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        offset = Offset(pxWidth, pxWidth),
                        size = Size(size.width - pxWidth* 2, size.height - pxWidth * 2)
                    ), cornerRadius = CornerRadius(pxRadius, pxRadius)
                )
            )
        }, operation = PathOperation.Difference)
    }
}

@Preview
@Composable
private fun _PreView() {
    AnimatedColorBorder(radius = 10.dp) {
        Box(modifier = Modifier.size(100.dp)) {
            Text(text = "TEST")
        }
    }
}