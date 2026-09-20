package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.data.DriverProfileViewModel
import com.example.ui.components.framerClickable
import com.example.ui.theme.DarkGray
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.iOSGreen
import com.example.ui.theme.iOSRed
import com.example.ui.utils.BiometricHelper

@Composable
fun ClimateScreen(
    navController: NavController,
    profileViewModel: DriverProfileViewModel = viewModel()
) {
    val context = LocalContext.current as FragmentActivity
    val activeProfile by profileViewModel.activeProfile.collectAsState()

    var isOn by remember { mutableStateOf(true) }
    var temperature by remember { mutableStateOf(72f) }
    var fanSpeed by remember { mutableStateOf(4f) }
    var isAcOn by remember { mutableStateOf(true) }
    var isAutoOn by remember { mutableStateOf(true) }
    var isRecircOn by remember { mutableStateOf(false) }
    var isFrontDefrostOn by remember { mutableStateOf(false) }
    
    // Seat warmers: 0 = off, 1 = low, 2 = med, 3 = high
    var driverSeatHeat by remember { mutableStateOf(1) }
    var passengerSeatHeat by remember { mutableStateOf(0) }

    // Sync initial temperature from active driver profile in Room DB
    LaunchedEffect(activeProfile?.id) {
        activeProfile?.let {
            temperature = it.climateTemp
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkGray)
                    .framerClickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = White)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Climate",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Interior/Exterior Temp & Power
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isOn) "Climate On" else "Climate Off",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOn) White else White.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Interior 74°F • Exterior 65°F",
                    fontSize = 14.sp,
                    color = White.copy(alpha = 0.6f)
                )
            }

            // Power Button with Biometric check
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (isOn) iOSBlue.copy(alpha = 0.2f) else DarkGray)
                    .framerClickable {
                        BiometricHelper.authenticate(
                            activity = context,
                            title = "Climate Control",
                            subtitle = "Authenticate to toggle climate state",
                            onSuccess = { isOn = !isOn },
                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                        )
                    },
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

        Spacer(modifier = Modifier.height(24.dp))

        // Sliders
        val tempColor = if (temperature > 74) Color(0xFFFF5252) else if (temperature < 68) Color(0xFF448AFF) else Color(0xFF66BB6A)

        ModernSlider(
            value = temperature,
            onValueChange = { if (isOn) temperature = it },
            valueRange = 60f..85f,
            activeColor = if (isOn) tempColor else DarkGray,
            inactiveColor = Color(0xFF2C2C2E),
            icon = Icons.Default.Thermostat,
            label = "Cabin Temperature",
            valueText = "${temperature.toInt()}°F"
        )

        Spacer(modifier = Modifier.height(16.dp))

        ModernSlider(
            value = fanSpeed,
            onValueChange = { if (isOn) fanSpeed = it },
            valueRange = 1f..10f,
            activeColor = if (isOn) White.copy(alpha = 0.8f) else DarkGray,
            inactiveColor = Color(0xFF2C2C2E),
            icon = Icons.Default.Air,
            label = "Fan Speed",
            valueText = "${fanSpeed.toInt()}"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Seat Warmers
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SeatWarmerButton(
                modifier = Modifier.weight(1f),
                label = "Driver Seat",
                level = if (isOn) driverSeatHeat else 0,
                onClick = {
                    if (isOn) driverSeatHeat = (driverSeatHeat + 1) % 4
                }
            )
            SeatWarmerButton(
                modifier = Modifier.weight(1f),
                label = "Passenger Seat",
                level = if (isOn) passengerSeatHeat else 0,
                onClick = {
                    if (isOn) passengerSeatHeat = (passengerSeatHeat + 1) % 4
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

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

        Spacer(modifier = Modifier.weight(1f))

        // Sync with Driver Profile Room DB
        activeProfile?.let { profile ->
            Button(
                onClick = {
                    profileViewModel.updateProfile(profile.copy(climateTemp = temperature))
                    Toast.makeText(context, "Saved ${temperature.toInt()}°F as default for ${profile.name}", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = DarkGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(26.dp))
            ) {
                Icon(Icons.Default.Save, contentDescription = null, tint = iOSBlue, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save as Default for ${profile.name}",
                    color = White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SeatWarmerButton(
    modifier: Modifier = Modifier,
    label: String,
    level: Int, // 0 = off, 1, 2, 3
    onClick: () -> Unit
) {
    val activeColor = when (level) {
        1 -> Color(0xFFFF9500)
        2 -> Color(0xFFFF5252)
        3 -> Color(0xFFFF2D55)
        else -> White.copy(alpha = 0.5f)
    }

    Box(
        modifier = modifier
            .framerClickable(onClick = onClick)
            .clip(RoundedCornerShape(20.dp))
            .background(DarkGray)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AirlineSeatReclineExtra,
                contentDescription = label,
                tint = activeColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, color = White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text(
                    text = if (level > 0) "Heat Level $level" else "Off",
                    color = activeColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
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
    
    val haptic = LocalHapticFeedback.current
    var lastHapticValue by remember { mutableStateOf(value.toInt()) }

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
                        
                        val newInt = newValue.toInt()
                        if (newInt != lastHapticValue) {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            lastHapticValue = newInt
                        }
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newFraction = (offset.x / width).coerceIn(0f, 1f)
                    val newValue = valueRange.start + newFraction * (valueRange.endInclusive - valueRange.start)
                    onValueChange(newValue)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    lastHapticValue = newValue.toInt()
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
                    fontSize = 17.sp
                )
            }
            Text(
                text = valueText,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
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
