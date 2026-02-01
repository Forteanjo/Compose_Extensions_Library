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

/**
 * Applies a fade effect over the content, drawing a gradient from an initial color to a final color.
 * This can be used to create effects like text fading into the background.
 *
 * @param initialColor The color at the start of the gradient. Defaults to [Color.Transparent].
 * @param fadeToColor The color at the end of the gradient. Defaults to [Color.White].
 * @param verticalFade If true, the gradient will be vertical (top to bottom). If false, it will be horizontal (left to right). Defaults to true.
 * @return A [Modifier] that draws a gradient over the content.
 */
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

/**
 * Applies a glowing effect around the composable.
 *
 * This modifier draws a shadow layer behind the composable, creating a "glow" effect.
 * The shape of the glow will match the rounded rectangle shape of the composable.
 *
 * @param glowingColor The color of the glow.
 * @param containerColor The color of the container itself. This is drawn to provide a surface for the shadow to appear behind. Defaults to [Color.White].
 * @param cornerRadius The corner radius for the rounded rectangle shape of both the container and the glow. Defaults to 0.dp for a sharp-cornered rectangle.
 * @param glowingRadius The blur radius of the glow. A larger value will create a more spread-out and softer glow. Defaults to 20.dp.
 * @param glowAlpha The alpha transparency of the glow. Defaults to 0.8f.
 * @param offsetX The horizontal offset of the glow. A positive value shifts the glow to the right, a negative value to the left. Defaults to 0.dp.
 * @param offsetY The vertical offset of the glow. A positive value shifts the glow downwards, a negative value upwards. Defaults to 0.dp.
 * @return A [Modifier] that applies the glow effect.
 */
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


/**
 * Creates a parallax effect on a composable as the user scrolls.
 * The composable's vertical position is adjusted based on the `scrollState`.
 *
 * This modifier is useful for creating depth in a scrollable layout, where some elements
 * (like a background image) move at a different speed than the foreground content.
 *
 * @param scrollState The [ScrollState] of the scrollable container. The parallax effect
 *                    is driven by changes in this state's value.
 * @param rate A factor to control the speed of the parallax effect. A higher rate results
 *             in a slower movement of the composable. A rate of 0 will cause the composable
 *             to move at the same speed as the scroll, while a negative rate is not recommended.
 *             The vertical offset is calculated as `scrollState.value / rate`.
 * @return A [Modifier] that applies the parallax layout behavior.
 */
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

/**
 * Applies a shimmering effect to the composable. This is often used to indicate a loading state.
 * The shimmer is a linear gradient that animates across the composable.
 *
 * This overload uses a predefined set of grey colors for the shimmer effect.
 *
 * @return A [Modifier] that adds the shimmer effect.
 */
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

/**
 * Applies a shimmering effect to a composable, typically used for loading placeholders.
 *
 * This modifier creates a shimmering animation by applying a linear gradient that moves across the composable.
 * The animation is an infinite loop, giving the appearance of a continuous shimmer.
 *
 * The gradient consists of three colors by default (`0xFFB8B5B5`, `0xFF6D6868`, `0xFFB8B5B5`)
 * to create a highlight effect that sweeps across the component.
 *
 * @return A [Modifier] that applies the shimmer effect.
 *
 * @sample
 * ```
 * Box(
 *     modifier = Modifier
 *         .size(100.dp, 20.dp)
 *         .clip(RoundedCornerShape(4.dp))
 *         .shimmerEffect()
 * )
 * ```
 */
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
