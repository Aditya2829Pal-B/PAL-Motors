package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.components.framerClickable
import com.example.ui.theme.DarkGray
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController) {
    var showBiometric by remember { mutableStateOf(false) }
    var authenticated by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = White,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .framerClickable { navController.popBackStack() }
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Settings",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                SettingsCard(
                    title = "Software Update",
                    subtitle = "Version 2026.4.1 is available (OTA)",
                    icon = Icons.Default.SystemUpdate,
                    onClick = { }
                )
            }
            item {
                SettingsCard(
                    title = "Driver Authentication",
                    subtitle = if (authenticated) "Verified (Biometrics)" else "Tap to authenticate",
                    icon = Icons.Default.Fingerprint,
                    onClick = { showBiometric = true },
                    valueText = if (authenticated) "Verified" else "Locked"
                )
            }
            item {
                SettingsCard(
                    title = "Voice Assistant",
                    subtitle = "Hands-free operations active",
                    icon = Icons.Default.Mic,
                    onClick = { }
                )
            }
            item {
                SettingsCard(
                    title = "Maintenance Alerts",
                    subtitle = "Tire pressure low on rear-right",
                    icon = Icons.Default.Warning,
                    iconColor = androidx.compose.ui.graphics.Color(0xFFFF9F0A),
                    onClick = { }
                )
            }
        }
    }

    if (showBiometric) {
        AlertDialog(
            onDismissRequest = { showBiometric = false },
            title = { Text("Biometric Authentication") },
            text = { Text("Verify your identity to unlock driver profiles and remote driving.") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        delay(500)
                        authenticated = true
                        showBiometric = false
                    }
                }) {
                    Text("Simulate FaceID", color = iOSBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBiometric = false }) {
                    Text("Cancel", color = White)
                }
            },
            containerColor = DarkGray,
            titleContentColor = White,
            textContentColor = White.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun SettingsCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: androidx.compose.ui.graphics.Color = White,
    valueText: String = "",
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .framerClickable(onClick = onClick)
            .clip(RoundedCornerShape(32.dp))
            .background(DarkGray)
            .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(32.dp))
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = White, fontSize = 16.sp)
            if (subtitle.isNotEmpty()) {
                Text(subtitle, color = White.copy(alpha = 0.6f), fontSize = 13.sp)
            }
        }
        if (valueText.isNotEmpty()) {
            Text(valueText, color = White.copy(alpha = 0.5f), fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = White.copy(alpha = 0.3f))
    }
}
