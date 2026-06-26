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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.DarkGray
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.White

@Composable
fun ClimateScreen(navController: NavController) {
    var temperature by remember { mutableStateOf(68) }
    var isOn by remember { mutableStateOf(true) }

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
            Text(
                "Top-Down Interior Render",
                color = White.copy(alpha = 0.3f),
                fontWeight = FontWeight.Medium
            )
        }
        
        // Bottom Control Panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(DarkGray)
                .padding(bottom = 48.dp, top = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Interior 74°F • Exterior 65°F",
                fontSize = 14.sp,
                color = White.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Power Button
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = "Power",
                    tint = if (isOn) iOSBlue else White.copy(alpha = 0.5f),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { isOn = !isOn }
                )
                
                // Temp Controls
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Decrease Temp",
                        tint = White,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { if (isOn) temperature-- }
                    )
                    
                    Text(
                        text = "$temperature°",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Light,
                        color = if (isOn) White else White.copy(alpha = 0.3f),
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Increase Temp",
                        tint = White,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { if (isOn) temperature++ }
                    )
                }
                
                // Seat Heater Icon (or Split)
                Icon(
                    imageVector = Icons.Default.Chair, // placeholder for seat heater
                    contentDescription = "Seat Heater",
                    tint = White.copy(alpha = 0.5f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
