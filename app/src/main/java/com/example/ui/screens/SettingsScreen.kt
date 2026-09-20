package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.ui.components.framerClickable
import com.example.ui.theme.DarkGray
import com.example.ui.theme.LightGray
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.iOSGreen
import com.example.ui.utils.BiometricHelper

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current as FragmentActivity
    var authenticated by remember { mutableStateOf(false) }
    var voiceAssistantEnabled by remember { mutableStateOf(true) }
    var showSoftwareDialog by remember { mutableStateOf(false) }
    var showMaintenanceDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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
                text = "Vehicle Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item {
                SettingsCard(
                    title = "Driver Profiles",
                    subtitle = "Manage Room database seating & climate preferences",
                    icon = Icons.Default.Person,
                    iconColor = iOSBlue,
                    onClick = { navController.navigate("profiles") }
                )
            }
            item {
                SettingsCard(
                    title = "Ambient Lighting",
                    subtitle = "LED color themes, mood presets & intensity",
                    icon = Icons.Default.Lightbulb,
                    iconColor = Color(0xFFAA00FF),
                    onClick = { navController.navigate("ambient") }
                )
            }
            item {
                SettingsCard(
                    title = "Software Update",
                    subtitle = "PAL OS Version 2026.4.1 (OTA)",
                    icon = Icons.Default.SystemUpdate,
                    iconColor = iOSGreen,
                    valueText = "Up to date",
                    onClick = { showSoftwareDialog = true }
                )
            }
            item {
                SettingsCard(
                    title = "Biometric Security",
                    subtitle = if (authenticated) "Hardware Biometrics Verified" else "Tap to authenticate with Fingerprint / Face",
                    icon = Icons.Default.Fingerprint,
                    iconColor = if (authenticated) iOSGreen else iOSBlue,
                    valueText = if (authenticated) "Verified" else "Locked",
                    onClick = {
                        BiometricHelper.authenticate(
                            activity = context,
                            title = "Driver Authentication",
                            subtitle = "Verify identity to confirm security status",
                            onSuccess = {
                                authenticated = true
                                Toast.makeText(context, "Driver Identity Authenticated", Toast.LENGTH_SHORT).show()
                            },
                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                        )
                    }
                )
            }
            item {
                SettingsCard(
                    title = "Voice Assistant",
                    subtitle = if (voiceAssistantEnabled) "Hands-free operations active ('Hey PAL')" else "Voice detection disabled",
                    icon = Icons.Default.Mic,
                    iconColor = if (voiceAssistantEnabled) iOSBlue else Color.Gray,
                    valueText = if (voiceAssistantEnabled) "Active" else "Off",
                    onClick = {
                        voiceAssistantEnabled = !voiceAssistantEnabled
                        Toast.makeText(context, if (voiceAssistantEnabled) "Voice Assistant Enabled" else "Voice Assistant Disabled", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            item {
                SettingsCard(
                    title = "Maintenance & Diagnostics",
                    subtitle = "Brake pads 88% • Cabin filter OK • 12V Nominal",
                    icon = Icons.Default.Build,
                    iconColor = Color(0xFFFF9F0A),
                    valueText = "Inspect",
                    onClick = { showMaintenanceDialog = true }
                )
            }
        }
    }

    if (showSoftwareDialog) {
        AlertDialog(
            onDismissRequest = { showSoftwareDialog = false },
            title = { Text("PAL OS Software 2026.4.1", color = White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Your vehicle software is up to date.", color = White, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Vico Real-time High Voltage Telemetry", color = White.copy(alpha = 0.7f), fontSize = 13.sp)
                    Text("• Predictive Range Estimation & Thermal Management", color = White.copy(alpha = 0.7f), fontSize = 13.sp)
                    Text("• Room DB Local Driver Profile Persistence", color = White.copy(alpha = 0.7f), fontSize = 13.sp)
                    Text("• Hardware Biometric Vehicle Access Layer", color = White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showSoftwareDialog = false }) {
                    Text("Close", color = iOSBlue)
                }
            },
            containerColor = DarkGray
        )
    }

    if (showMaintenanceDialog) {
        AlertDialog(
            onDismissRequest = { showMaintenanceDialog = false },
            title = { Text("Diagnostic Health", color = White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    DiagnosticItem(label = "Brake Friction Material", value = "88% Life", statusColor = iOSGreen)
                    DiagnosticItem(label = "Cabin HEPA Filter", value = "Good Condition", statusColor = iOSGreen)
                    DiagnosticItem(label = "Low Voltage (12V) Li-Ion", value = "14.2V (Nominal)", statusColor = iOSGreen)
                    DiagnosticItem(label = "Drive Unit Coolant Loop", value = "Flow Rate 12.4 L/min", statusColor = iOSGreen)
                    DiagnosticItem(label = "Tire Pressure Sensors", value = "BLE Monitored Active", statusColor = iOSBlue)
                }
            },
            confirmButton = {
                TextButton(onClick = { showMaintenanceDialog = false }) {
                    Text("Done", color = iOSBlue)
                }
            },
            containerColor = DarkGray
        )
    }
}

@Composable
fun DiagnosticItem(label: String, value: String, statusColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = White.copy(alpha = 0.8f), fontSize = 13.sp)
        Text(value, color = statusColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
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
            .clip(RoundedCornerShape(24.dp))
            .background(DarkGray)
            .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold, color = White, fontSize = 16.sp)
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(subtitle, color = White.copy(alpha = 0.6f), fontSize = 12.sp)
            }
        }
        if (valueText.isNotEmpty()) {
            Text(valueText, color = White.copy(alpha = 0.5f), fontSize = 13.sp, modifier = Modifier.padding(end = 8.dp))
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = White.copy(alpha = 0.3f))
    }
}
