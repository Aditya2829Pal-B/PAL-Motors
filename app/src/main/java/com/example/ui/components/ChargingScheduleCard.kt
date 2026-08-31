package com.example.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.ui.theme.White
import com.example.ui.theme.iOSGreen

@Composable
fun ChargingScheduleCard(modifier: Modifier = Modifier) {
    var isEnabled by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    
    // Simulated time selections
    var startTime by remember { mutableStateOf("12:00 AM") }
    var endTime by remember { mutableStateOf("06:00 AM") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .framerClickable(onClick = { isExpanded = !isExpanded })
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
                    imageVector = Icons.Default.EvStation,
                    contentDescription = null,
                    tint = if (isEnabled) iOSGreen else White.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Scheduled Charging",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                    Text(
                        text = if (isEnabled) "Starts at $startTime" else "Not scheduled",
                        fontSize = 13.sp,
                        color = if (isEnabled) iOSGreen else White.copy(alpha = 0.6f)
                    )
                }
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = { 
                    isEnabled = it
                    if (it) isExpanded = true 
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = White,
                    checkedTrackColor = iOSGreen,
                    uncheckedThumbColor = White.copy(alpha = 0.6f),
                    uncheckedTrackColor = Color(0xFF2C2C2E)
                )
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Off-Peak Windows",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = White.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TimeSelector(
                    label = "Start Time", 
                    time = startTime, 
                    onClick = { 
                        // Simulate toggling through preset times for demonstration
                        startTime = if (startTime == "12:00 AM") "09:00 PM" else "12:00 AM" 
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                TimeSelector(
                    label = "End Time", 
                    time = endTime, 
                    onClick = {
                        endTime = if (endTime == "06:00 AM") "08:00 AM" else "06:00 AM"
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Vehicle will delay charging until $startTime to minimize energy costs based on your local utility rates.",
                fontSize = 12.sp,
                color = White.copy(alpha = 0.5f),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun RowScope.TimeSelector(label: String, time: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .framerClickable(onClick = onClick)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2C2C2E))
            .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = White.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = time,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = White
        )
    }
}
