package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.components.framerClickable
import com.example.ui.theme.DarkGray
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.White

@Composable
fun ClimateScreen(navController: NavController) {
    var temperature by remember { mutableStateOf(68f) }
    var isOn by remember { mutableStateOf(true) }
    var fanSpeed by remember { mutableStateOf(4f) }
    var isAcOn by remember { mutableStateOf(true) }
    var isAutoOn by remember { mutableStateOf(false) }
    var isRecircOn by remember { mutableStateOf(true) }
    var isFrontDefrostOn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown, // or Back
                contentDescription = "Close",
                tint = White,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.popBackStack() }
            )
        }
        
        // 3D Car Interior Top-Down Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            com.example.ui.components.CarTopDown(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.9f)
            )
        }
        
        // Bottom Control Panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color(0xFF1C1C1E))
                .padding(bottom = 48.dp, top = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Climate Control",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Text(
                        text = "Interior 74°F • Exterior 65°F",
                        fontSize = 14.sp,
                        color = White.copy(alpha = 0.6f)
                    )
                }
                
                // Power Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(if (isOn) iOSBlue.copy(alpha = 0.2f) else DarkGray)
                        .framerClickable { isOn = !isOn },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "Power",
                        tint = if (isOn) iOSBlue else White.copy(alpha = 0.5f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Sliders
            val tempColor = if (temperature > 72) Color(0xFFFF5252) else if (temperature < 65) Color(0xFF448AFF) else Color(0xFF66BB6A)
            
            ModernSlider(
                value = temperature,
                onValueChange = { if (isOn) temperature = it },
                valueRange = 60f..85f,
                activeColor = if (isOn) tempColor else DarkGray,
                inactiveColor = Color(0xFF2C2C2E),
                icon = Icons.Default.Thermostat,
                label = "Temperature",
                valueText = "${temperature.toInt()}°"
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            ModernSlider(
                value = fanSpeed,
                onValueChange = { if (isOn) fanSpeed = it },
                valueRange = 1f..10f,
                activeColor = if (isOn) White.copy(alpha = 0.8f) else DarkGray,
                inactiveColor = Color(0xFF2C2C2E),
                icon = Icons.Default.Air,
                label = "Airflow Intensity",
                valueText = "${fanSpeed.toInt()}"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Quick Actions (A/C, Auto, Recirculate, Defrost)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionButton(
                    icon = Icons.Default.AcUnit, 
                    label = "A/C", 
                    isActive = isAcOn && isOn, 
                    onClick = { if (isOn) isAcOn = !isAcOn }
                )
                QuickActionButton(
                    icon = Icons.Default.AutoMode, 
                    label = "Auto", 
                    isActive = isAutoOn && isOn, 
                    onClick = { if (isOn) isAutoOn = !isAutoOn }
                )
                QuickActionButton(
                    icon = Icons.Default.Loop, 
                    label = "Recirc", 
                    isActive = isRecircOn && isOn, 
                    onClick = { if (isOn) isRecircOn = !isRecircOn }
                )
                QuickActionButton(
                    icon = Icons.Default.Air, 
                    label = "Defrost", 
                    isActive = isFrontDefrostOn && isOn, 
                    onClick = { if (isOn) isFrontDefrostOn = !isFrontDefrostOn }
                )
            }
        }
    }
}

@Composable
fun ModernSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.DarkGray,
    icon: ImageVector? = null,
    label: String = "",
    valueText: String = ""
) {
    var width by remember { mutableStateOf(1f) }
    val fraction = ((value - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(inactiveColor)
            .onSizeChanged { width = it.width.toFloat().coerceAtLeast(1f) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        change.consume()
                        val newX = change.position.x
                        val newFraction = (newX / width).coerceIn(0f, 1f)
                        val newValue = valueRange.start + newFraction * (valueRange.endInclusive - valueRange.start)
                        onValueChange(newValue)
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newFraction = (offset.x / width).coerceIn(0f, 1f)
                    val newValue = valueRange.start + newFraction * (valueRange.endInclusive - valueRange.start)
                    onValueChange(newValue)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = fraction)
                .background(activeColor)
        )
        
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        imageVector = icon, 
                        contentDescription = null, 
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Text(
                    text = label,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            }
            Text(
                text = valueText,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.framerClickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(if (isActive) iOSBlue else Color(0xFF2C2C2E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = White,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = if (isActive) White else White.copy(alpha = 0.6f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
