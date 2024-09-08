package com.devtutorial.richlinks.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

/**
 * Applies a shimmer effect to the Modifier, creating a shimmering animation that is typically used
 * as a placeholder for loading content.
 *
 * Reference: [Shimmer & Shadow Loading Effect Animation with Jetpack Compose](https://proandroiddev.com/shimmer-shadow-loading-effect-animation-with-jetpack-compose-f4b3de28dc2b).
 *
 * @param widthOfShadowBrush The width of the shimmer brush used to create the gradient effect.
 * @param angleOfAxisY The angle of the axis Y for the gradient.
 * @param durationMillis The duration of the shimmer animation in milliseconds.
 * @return A Modifier that applies the shimmer effect.
 */

fun Modifier.shimmer(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    return composed {

        val shimmerColors = listOf(
            MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
            Color.LightGray.copy(alpha = 1.0f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
            MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
        )

        val transition = rememberInfiniteTransition(label = "shimmer transition")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Shimmer loading animation",
        )

        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                end = Offset(x = translateAnimation.value, y = angleOfAxisY),
            ),
            shape = RectangleShape
        )
    }
}