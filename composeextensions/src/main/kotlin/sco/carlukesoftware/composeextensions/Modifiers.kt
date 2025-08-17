package sco.carlukesoftware.composeextensions

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.annotation.FloatRange
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import android.graphics.Paint as NativePaint

/**
 * Applies the given [Modifier] if the [condition] is true.
 *
 * @param condition The condition to check.
 * @param then The [Modifier] to apply if the [condition] is true.
 * @return The original [Modifier] if the [condition] is false, otherwise the [Modifier] returned by [then].
 */
fun Modifier.iif(
    condition: Boolean,
    then: Modifier.() -> Modifier
): Modifier =
    if (condition) {
        then()
    } else {
        this
    }

/**
 * Applies a [Modifier] if the given [predicate] is true.
 *
 * @param predicate The condition to check.
 * @param modify The [Modifier] to apply if [predicate] is true.
 * @return The original [Modifier] if [predicate] is false, otherwise the result of applying [modify] to the original [Modifier].
 */
fun Modifier.thenIf(
    predicate: Boolean,
    modify: () -> Modifier
): Modifier {
    return this.then(if (predicate) modify() else Modifier)
}

/**
 * Draws a shadow behind the content.
 *
 * @param color The color of the shadow.
 * @param offsetX The horizontal offset of the shadow.
 * @param offsetY The vertical offset of the shadow.
 * @param blurRadius The blur radius of the shadow.
 *
 * @return A [Modifier] that draws the shadow behind the content.
 */
fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
) = drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()
            val rightPixel = size.width + topPixel
            val bottomPixel = size.height + leftPixel

            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
        }
    }


/**
 * A workaround for custom colored shadow.
 */
fun Modifier.shadow(
    spread: Dp = 8.dp,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = .25f,
    color: Color = Color.Gray,
    radius: Dp = 8.dp
): Modifier {
    val spreadLayer = spread.value.toInt()

    var modifier = this

    for (x in spreadLayer downTo 1) {
        modifier = modifier
            .border(
                width = 1.dp,
                color = color.copy(alpha / x),
                shape = RoundedCornerShape(radius + x.dp)
            )
            .padding(1.dp)
    }

    return modifier
}

/**
 * Adds an inner shadow effect to the content.
 *
 * @param shape The shape of the shadow.
 * @param color The color of the shadow.
 * @param blur The blur radius of the shadow.
 * @param offsetY The shadow offset along the Y-axis.
 * @param offsetX The shadow offset along the X-axis.
 * @param spread The amount to expand the shadow beyond its size.
 *
 * @return A modified Modifier with the inner shadow effect applied.
 */
fun Modifier.innerShadow(
    shape: Shape,
    color: Color = Color.Black,
    blur: Dp = 4.dp,
    offsetY: Dp = 2.dp,
    offsetX: Dp = 2.dp,
    spread: Dp = 0.dp
) = this.drawWithContent {

    drawContent()

    drawIntoCanvas { canvas ->

        val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        val paint = Paint()
        paint.color = color

        canvas.saveLayer(size.toRect(), paint)
        canvas.drawOutline(shadowOutline, paint)

        paint.asFrameworkPaint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            if (blur.toPx() > 0) {
                maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
            }
        }

        paint.color = Color.Black

        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

fun Modifier.fadeOut(
    initialColor: Color = Color.Transparent,
    fadeToColor: Color = Color.White,
    verticalFade: Boolean = true
) = drawWithContent {
        val colors = listOf(initialColor, fadeToColor)
        drawContent()
        drawRect(
            brush = if (verticalFade) Brush.verticalGradient(colors) else Brush.horizontalGradient(colors)
        )
    }

fun Modifier.glow(
    glowingColor: Color,
    containerColor: Color = Color.White,
    cornerRadius: Dp = 0.dp,
    glowingRadius: Dp = 20.dp,
    glowAlpha: Float = 0.8f,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp
) = drawBehind {
        val size = this.size
        drawContext
            .canvas
            .nativeCanvas
            .apply {
                drawRoundRect(0f, 0f, size.width, size.height, cornerRadius.toPx(), cornerRadius.toPx(),
                    NativePaint().apply {
                            color = containerColor.toArgb()
                            setShadowLayer(
                                glowingRadius.toPx(),
                                offsetX.toPx(), offsetY.toPx(),
                                glowingColor.copy(alpha = glowAlpha).toArgb()
                            )
                        }

                )
            }
    }


fun Modifier.parallaxLayout(scrollState: ScrollState, rate: Int) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints = constraints)
        val height = if (rate > 0) scrollState.value / rate else scrollState.value
        layout(
            width = placeable.width,
            height = placeable.height
        ) {
            placeable.place(
                x = 0,
                y = height)
        }

    }

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition
        .animateFloat(
            initialValue = -2 * size.width.toFloat(),
            targetValue = 2 * size.width.toFloat(),
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 1000)
            ), label = ""
        )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF6D6868),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )

        .onGloballyPositioned {
            size = it.size
        }
}

fun Modifier.shimmerEffect(
    baseColor: Color = Color.LightGray
): Modifier = composed {
    val transition = rememberInfiniteTransition(
        label = "shimmer-transition"
    )

    val shimmerTranslate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer-translate"
    )

    drawWithCache {
        val shimmerColors = listOf(
            baseColor.copy(alpha = 0.6f),
            baseColor.copy(alpha = 0.2f),
            baseColor.copy(alpha = 0.6f)
        )

        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(shimmerTranslate - size.width, 0f),
            end = Offset(shimmerTranslate, size.height)
        )

        onDrawBehind {
            drawRect(brush)
        }
    }
}

/**
 * Extension function for [Modifier] that detects tap gestures and calculates the
 * tap's origin as a [TransformOrigin], which represents the position as a fraction
 * of the size of the element.
 *
 * @param onTap A lambda function that receives a [TransformOrigin] representing the
 * fraction of the tap position relative to the width and height of the element.
 *
 * @return The [Modifier] with added tap detection behavior.
 */
fun Modifier.detectTapWithOrigin(
    onTap: (TransformOrigin) -> Unit
) = this.pointerInput(Unit) {
    detectTapGestures { clickOffset ->
        val origin = TransformOrigin(
            pivotFractionX = clickOffset.x / size.width,
            pivotFractionY = clickOffset.y / size.height,
        )
        onTap(origin)
    }
}

//--------------------------------------------------------------------------------
