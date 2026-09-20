package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGray
import com.example.ui.theme.LightGray
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.iOSGreen
import kotlinx.coroutines.delay

@Composable
fun TirePressureCard(modifier: Modifier = Modifier) {
    // Simulated real-time pressure
    var fl by remember { mutableStateOf(35) }
    var fr by remember { mutableStateOf(35) }
    var rl by remember { mutableStateOf(35) }
    var rr by remember { mutableStateOf(32) } // Starts lower, simulating pressure leak
    var isExpanded by remember { mutableStateOf(false) }

    // Real-time pressure leak simulation
    LaunchedEffect(Unit) {
        while (rr > 26) {
            delay(2500)
            rr -= 1
        }
    }

    val hasWarning = rr < 32 || fl < 32 || fr < 32 || rl < 32
    val hasCritical = rr < 28 || fl < 28 || fr < 28 || rl < 28

    val statusColor by animateColorAsState(
        targetValue = when {
            hasCritical -> Color(0xFFFF453A) // Critical Red
            hasWarning -> Color(0xFFFF9F0A)  // Warning Yellow
            else -> iOSGreen
        },
        label = "statusColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (hasWarning) statusColor.copy(alpha = 0.5f) else White.copy(alpha = 0.05f),
        label = "borderColor"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .framerClickable(onClick = { isExpanded = !isExpanded })
            .clip(RoundedCornerShape(32.dp))
            .background(DarkGray)
            .border(if (hasWarning) 2.dp else 1.dp, borderColor, RoundedCornerShape(32.dp))
            .padding(24.dp)
            .animateContentSize()
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (hasWarning) Icons.Default.Warning else Icons.Default.Speed,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Tire Pressure (TPMS)",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                    Text(
                        text = when {
                            hasCritical -> "Critical low pressure detected"
                            hasWarning -> "Warning: Check right rear tire"
                            else -> "All 4 tires nominal (35 PSI)"
                        },
                        fontSize = 13.sp,
                        color = statusColor
                    )
                }
            }
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(24.dp))

            // 4 quadrants for tires
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TireIndicator(label = "FL", psi = fl)
                    Spacer(modifier = Modifier.height(28.dp))
                    TireIndicator(label = "RL", psi = rl)
                }

                // Vehicle silhouette graphic
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(130.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF242426))
                        .border(1.dp, White.copy(alpha = 0.1f), RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "TOP\nVIEW",
                        color = White.copy(alpha = 0.25f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    TireIndicator(label = "FR", psi = fr)
                    Spacer(modifier = Modifier.height(28.dp))
                    TireIndicator(label = "RR", psi = rr)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Inflate / Recalibrate button
            Button(
                onClick = {
                    fl = 35
                    fr = 35
                    rl = 35
                    rr = 35
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = LightGray)
            ) {
                Icon(Icons.Default.Build, contentDescription = null, tint = White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Inflate & Reset TPMS Sensors", color = White, fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun TireIndicator(label: String, psi: Int) {
    val isLow = psi < 32
    val isCritical = psi < 28

    val color by animateColorAsState(
        targetValue = when {
            isCritical -> Color(0xFFFF453A)
            isLow -> Color(0xFFFF9F0A)
            else -> White
        },
        label = "tireColor"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = White.copy(alpha = 0.5f))
        Text(
            text = "$psi",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(text = "PSI", fontSize = 10.sp, color = color.copy(alpha = 0.7f))
    }
}
