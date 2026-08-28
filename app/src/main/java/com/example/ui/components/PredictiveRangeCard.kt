package com.example.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGray
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.iOSGreen
import kotlinx.coroutines.delay

@Composable
fun PredictiveRangeCard(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    // Simulated Telemetry Data
    val currentBatteryPercent = 78
    val maxRange = 310 // miles at 100%
    val nominalRange = (maxRange * (currentBatteryPercent / 100f)).toInt()
    
    // Predictive Factors (Simulated)
    val tempImpact = -5 // Cold weather
    val drivingHistoryImpact = -12 // Aggressive driving
    val climateImpact = -8 // A/C is on
    
    val predictedRange = nominalRange + tempImpact + drivingHistoryImpact + climateImpact

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .framerClickable(onClick = { expanded = !expanded })
            .clip(RoundedCornerShape(32.dp))
            .background(DarkGray)
            .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(32.dp))
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
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = iOSBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Predictive Range",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                    Text(
                        text = "Based on history & telemetry",
                        fontSize = 13.sp,
                        color = White.copy(alpha = 0.6f)
                    )
                }
            }
            Text(
                text = "$predictedRange mi",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Battery Status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Current Charge ($currentBatteryPercent%)", fontSize = 14.sp, color = White.copy(alpha = 0.8f))
                Text(text = "$nominalRange mi", fontSize = 14.sp, color = White)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Active Factors",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = White.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Factors
            PredictiveFactorRow(
                icon = Icons.Default.Thermostat,
                label = "External Temp (42°F)",
                value = tempImpact,
                color = Color(0xFF448AFF) // Blue for cold
            )
            PredictiveFactorRow(
                icon = Icons.Default.Speed,
                label = "Recent Driving History",
                value = drivingHistoryImpact,
                color = Color(0xFFFF5252) // Red for aggressive
            )
            PredictiveFactorRow(
                icon = Icons.Default.AcUnit,
                label = "Climate Control Active",
                value = climateImpact,
                color = Color(0xFFFFC107) // Yellow
            )
        }
    }
}

@Composable
fun PredictiveFactorRow(icon: ImageVector, label: String, value: Int, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = White.copy(alpha = 0.8f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${if (value > 0) "+" else ""}$value mi",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}
