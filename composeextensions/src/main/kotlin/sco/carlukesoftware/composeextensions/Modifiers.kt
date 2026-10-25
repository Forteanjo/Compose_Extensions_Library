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
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import android.graphics.Paint as NativePaint

/**
 * Applies a [Modifier] if the given [condition] is true.
 *
 * Marked `inline` to avoid lambda allocation at the call site.
 *
 * @param condition The condition to evaluate.
 * @param block The modifier builder lambda to run if [condition] is true.
 * @return The updated [Modifier].
 */
inline fun Modifier.thenIf(
    condition: Boolean,
    block: Modifier.() -> Modifier
): Modifier = if (condition) then(block()) else this

/**
 * Alias for [thenIf]. Applies the given [then] modifier builder if [condition] is true.
 */
inline fun Modifier.iif(
    condition: Boolean,
    then: Modifier.() -> Modifier
): Modifier = thenIf(condition, then)

/**
 * Draws a custom drop shadow behind the content using hardware-accelerated/canvas blur.
 *
 * Uses [drawWithCache] to avoid paint and mask filter allocations on every frame.
 *
 * @param color The color of the shadow.
 * @param offsetX The horizontal offset of the shadow.
 * @param offsetY The vertical offset of the shadow.
 * @param blurRadius The blur radius of the shadow.
 */
fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
): Modifier = drawWithCache {
    val paint = Paint().apply {
        this.color = color
    }
    val frameworkPaint = paint.asFrameworkPaint()
    if (blurRadius > 0.dp) {
        frameworkPaint.maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
    }

    val leftPixel = offsetX.toPx()
    val topPixel = offsetY.toPx()
    val rightPixel = size.width + leftPixel
    val bottomPixel = size.height + topPixel

    onDrawBehind {
        drawIntoCanvas { canvas ->
            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                paint = paint,
            )
        }
    }
}

/**
 * Creates a layered spread shadow effect around the composable using repeated border steps.
 *
 * Renamed to [spreadShadow] to prevent ambiguity with standard Compose elevation shadows.
 */
fun Modifier.spreadShadow(
    spread: Dp = 8.dp,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 0.25f,
    color: Color = Color.Gray,
    radius: Dp = 8.dp
): Modifier {
    val spreadLayer = spread.value.toInt()
    var modifier = this

    for (x in spreadLayer downTo 1) {
        modifier = modifier
            .border(
                width = 1.dp,
                color = color.copy(alpha = alpha / x),
                shape = RoundedCornerShape(radius + x.dp)
            )
            .padding(1.dp)
    }

    return modifier
}

/**
 * Adds an inner shadow effect inside the content outline.
 *
 * Uses [drawWithCache] to cache the outline and paint objects across draw calls.
 */
fun Modifier.innerShadow(
    shape: Shape,
    color: Color = Color.Black,
    blur: Dp = 4.dp,
    offsetY: Dp = 2.dp,
    offsetX: Dp = 2.dp,
    spread: Dp = 0.dp
): Modifier = drawWithCache {
    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint().apply {
        this.color = color
    }
    val frameworkPaint = paint.asFrameworkPaint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        if (blur.toPx() > 0) {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    onDrawWithContent {
        drawContent()
        drawIntoCanvas { canvas ->
            canvas.saveLayer(size.toRect(), paint)
            canvas.drawOutline(shadowOutline, paint)

            paint.color = Color.Black
            canvas.translate(offsetX.toPx(), offsetY.toPx())
            canvas.drawOutline(shadowOutline, paint)
            canvas.restore()
        }
    }
}

/**
 * Applies a linear gradient fade overlay across the content.
 *
 * Caches the gradient brush in [drawWithCache] to minimize draw allocations.
 */
fun Modifier.fadeOut(
    initialColor: Color = Color.Transparent,
    fadeToColor: Color = Color.White,
    verticalFade: Boolean = true
): Modifier = drawWithCache {
    val colors = listOf(initialColor, fadeToColor)
    val brush = if (verticalFade) {
        Brush.verticalGradient(colors)
    } else {
        Brush.horizontalGradient(colors)
    }

    onDrawWithContent {
        drawContent()
        drawRect(brush = brush)
    }
}

/**
 * Applies a glowing shadow effect around the composable surface.
 */
fun Modifier.glow(
    glowingColor: Color,
    containerColor: Color = Color.White,
    cornerRadius: Dp = 0.dp,
    glowingRadius: Dp = 20.dp,
    glowAlpha: Float = 0.8f,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp
): Modifier = drawWithCache {
    val nativePaint = NativePaint().apply {
        color = containerColor.toArgb()
        setShadowLayer(
            glowingRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            glowingColor.copy(alpha = glowAlpha).toArgb()
        )
    }

    onDrawBehind {
        drawContext.canvas.nativeCanvas.drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            cornerRadius.toPx(),
            cornerRadius.toPx(),
            nativePaint
        )
    }
}

/**
 * Creates a parallax scrolling translation effect without re-measuring layout during scroll.
 *
 * Leverages [graphicsLayer] to update GPU translation directly.
 */
fun Modifier.parallaxLayout(
    scrollState: ScrollState,
    rate: Float = 2f
): Modifier = graphicsLayer {
    translationY = if (rate != 0f) -scrollState.value / rate else 0f
}

/**
 * Applies an animated shimmer loading effect across the composable.
 *
 * @param baseColor Base placeholder color.
 * @param highlightColor Shimmer highlight pulse color.
 * @param durationMillis Animation loop duration in milliseconds.
 */
fun Modifier.shimmerEffect(
    baseColor: Color = Color(0xFFB8B5B5),
    highlightColor: Color = Color(0xFFE0E0E0),
    durationMillis: Int = 1000
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    val colors = listOf(baseColor, highlightColor, baseColor)

    drawWithCache {
        val width = size.width
        val height = size.height
        val totalDistance = width * 2f

        onDrawBehind {
            val startX = -width + (translateProgress * totalDistance)
            val brush = Brush.linearGradient(
                colors = colors,
                start = Offset(startX, 0f),
                end = Offset(startX + width, height)
            )
            drawRect(brush = brush)
        }
    }
}

/**
 * Detects tap gestures and passes the click origin relative to the element's width and height.
 */
fun Modifier.detectTapWithOrigin(
    onTap: (TransformOrigin) -> Unit
): Modifier = pointerInput(onTap) {
    detectTapGestures { clickOffset ->
        if (size.width > 0 && size.height > 0) {
            val origin = TransformOrigin(
                pivotFractionX = clickOffset.x / size.width,
                pivotFractionY = clickOffset.y / size.height,
            )
            onTap(origin)
        }
    }
}
