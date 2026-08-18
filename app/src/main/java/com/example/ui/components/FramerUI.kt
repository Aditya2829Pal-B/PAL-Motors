package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

@Composable
fun Modifier.framerClickable(onClick: () -> Unit): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    var size by remember { mutableStateOf(IntSize.Zero) }
    var touchPosition by remember { mutableStateOf(Offset.Zero) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f, // Bouncy spring
            stiffness = Spring.StiffnessLow
        ),
        label = "framer_scale"
    )

    // Calculate 3D rotation based on touch position
    val targetRotationX = if (isPressed && size.height > 0) {
        val centerY = size.height / 2f
        val maxRotation = 12f // Maximum tilt angle in degrees
        // Pressing bottom (y > centerY) should tilt bottom away -> rotationX negative
        ((touchPosition.y - centerY) / centerY).coerceIn(-1f, 1f) * -maxRotation
    } else 0f

    val targetRotationY = if (isPressed && size.width > 0) {
        val centerX = size.width / 2f
        val maxRotation = 12f
        // Pressing right (x > centerX) depresses right side -> left comes forward -> rotationY positive
        ((touchPosition.x - centerX) / centerX).coerceIn(-1f, 1f) * maxRotation
    } else 0f

    val animatedRotationX by animateFloatAsState(
        targetValue = targetRotationX,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "rotationX"
    )
    
    val animatedRotationY by animateFloatAsState(
        targetValue = targetRotationY,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "rotationY"
    )

    this
        .onSizeChanged { size = it }
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val change = event.changes.firstOrNull()
                    if (change != null && change.pressed) {
                        touchPosition = change.position
                    }
                }
            }
        }
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            rotationX = animatedRotationX
            rotationY = animatedRotationY
            cameraDistance = 12f * density
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null, // Framer design usually removes ripple in favor of scale bounce
            onClick = onClick
        )
}
