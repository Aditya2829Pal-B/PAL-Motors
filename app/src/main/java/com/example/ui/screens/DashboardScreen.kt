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
import com.example.data.DriverProfileViewModel
import com.example.ui.components.framerClickable
import com.example.ui.theme.DarkGray
import com.example.ui.theme.LightGray
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.iOSGreen
import com.example.ui.utils.BiometricHelper

@Composable
fun DashboardScreen(navController: NavController, profileViewModel: DriverProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var isLocked by remember { mutableStateOf(true) }
    var showVehicleControls by remember { mutableStateOf(false) }
    var frunkOpen by remember { mutableStateOf(false) }
    var trunkOpen by remember { mutableStateOf(false) }
    var windowsVented by remember { mutableStateOf(false) }

    val context = LocalContext.current as FragmentActivity
    val activeProfile by profileViewModel.activeProfile.collectAsState()
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = activeProfile?.name?.let { "${it}'s Model S" } ?: "PAL Motors Model S",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "282 mi • Battery 78%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = White.copy(alpha = 0.6f)
                    )
                    Text(
                        text = if (isLocked) "Parked • Locked" else "Parked • Unlocked",
                        fontSize = 14.sp,
                        color = if (isLocked) White.copy(alpha = 0.6f) else iOSGreen
                    )
                }
                
                // Profile Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkGray)
                        .framerClickable { navController.navigate("profiles") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = White.copy(alpha = 0.8f))
                }
            }
        }

        item {
            // Car Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.Center
            ) {
                com.example.ui.components.CarSideProfile(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                )
            }
        }

        item {
            // Quick Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickControlFramer(
                    icon = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                    label = if (isLocked) "Lock" else "Unlock",
                    onClick = { 
                        BiometricHelper.authenticate(
                            activity = context,
                            title = if (isLocked) "Authenticate to Unlock" else "Lock Vehicle",
                            subtitle = "Verify identity to change vehicle lock state",
                            onSuccess = { 
                                isLocked = !isLocked 
                                Toast.makeText(context, if (isLocked) "Vehicle Locked" else "Vehicle Unlocked", Toast.LENGTH_SHORT).show()
                            },
                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                        )
                    }
                )

                QuickControlFramer(
                    icon = Icons.Default.AcUnit,
                    label = "Climate",
                    onClick = { navController.navigate("climate") }
                )

                QuickControlFramer(
                    icon = Icons.Default.ElectricBolt,
                    label = "Start",
                    onClick = { 
                        BiometricHelper.authenticate(
                            activity = context,
                            title = "Remote Vehicle Start",
                            subtitle = "Verify identity to enable keyless driving",
                            onSuccess = { Toast.makeText(context, "Keyless Driving Active (2 min)", Toast.LENGTH_SHORT).show() },
                            onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                        )
                    }
                )

                QuickControlFramer(
                    icon = Icons.Default.DirectionsCar,
                    label = "Controls",
                    onClick = { showVehicleControls = true }
                )
            }
        }
        
        item {
            // Real-Time Diagnostic Telemetry Chart with Vico
            com.example.ui.components.BatteryDiagnosticChart()
        }
        
        item {
            com.example.ui.components.PredictiveRangeCard()
        }
        
        item {
            com.example.ui.components.TirePressureCard()
        }

        item {
            com.example.ui.components.ChargingScheduleCard()
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Menu Items
        item { MenuListItem(icon = Icons.Default.Thermostat, title = "Climate", subtitle = "Active • Interior 74°F • Dual Zone", onClick = { navController.navigate("climate") }) }
        item { MenuListItem(icon = Icons.Default.Person, title = "Driver Profiles", subtitle = "${activeProfile?.name ?: "Aditya"} • Seat ${activeProfile?.seatPosition?.toInt() ?: 65}%", onClick = { navController.navigate("profiles") }) }
        item { MenuListItem(icon = Icons.Default.Lightbulb, title = "Ambient Lighting", subtitle = "Mood presets & interior glow", onClick = { navController.navigate("ambient") }) }
        item { MenuListItem(icon = Icons.Default.LocationOn, title = "Location & Summon", subtitle = "123 Main St • Summon vehicle", onClick = { navController.navigate("map") }) }
        item { MenuListItem(icon = Icons.Default.Tune, title = "Vehicle Settings", subtitle = "Software OTA, Security, Telemetry", onClick = { navController.navigate("settings") }) }
        
        item {
            Spacer(modifier = Modifier.height(36.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "PAL MOTORS • MODEL S",
                    fontSize = 13.sp,
                    letterSpacing = 4.sp,
                    color = White.copy(alpha = 0.3f)
                )
            }
        }
    }

    if (showVehicleControls) {
        AlertDialog(
            onDismissRequest = { showVehicleControls = false },
            title = {
                Text("Quick Vehicle Controls", color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        VehicleActionTile(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Campaign,
                            title = "Honk Horn",
                            onClick = { Toast.makeText(context, "Vehicle Horn Sounded", Toast.LENGTH_SHORT).show() }
                        )
                        VehicleActionTile(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.FlashOn,
                            title = "Flash Lights",
                            onClick = { Toast.makeText(context, "Headlights Flashed", Toast.LENGTH_SHORT).show() }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        VehicleActionTile(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.DirectionsCar,
                            title = if (frunkOpen) "Close Frunk" else "Open Frunk",
                            onClick = {
                                frunkOpen = !frunkOpen
                                Toast.makeText(context, if (frunkOpen) "Front Trunk Opened" else "Front Trunk Closed", Toast.LENGTH_SHORT).show()
                            }
                        )
                        VehicleActionTile(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Luggage,
                            title = if (trunkOpen) "Close Trunk" else "Open Trunk",
                            onClick = {
                                trunkOpen = !trunkOpen
                                Toast.makeText(context, if (trunkOpen) "Rear Trunk Opened" else "Rear Trunk Closed", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                    VehicleActionTile(
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.Air,
                        title = if (windowsVented) "Close Windows" else "Vent Windows (3in)",
                        onClick = {
                            windowsVented = !windowsVented
                            Toast.makeText(context, if (windowsVented) "Windows Vented" else "Windows Fully Closed", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showVehicleControls = false }) {
                    Text("Done", color = iOSBlue, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = DarkGray
        )
    }
}

@Composable
fun VehicleActionTile(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .framerClickable(onClick = onClick)
            .clip(RoundedCornerShape(16.dp))
            .background(LightGray)
            .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(16.dp))
            .padding(vertical = 14.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = White, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, color = White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun QuickControlFramer(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String = "",
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .framerClickable(onClick = onClick)
                .clip(RoundedCornerShape(24.dp))
                .background(DarkGray)
                .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = White,
                modifier = Modifier.size(28.dp)
            )
        }
        if (label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(label, fontSize = 12.sp, color = White.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun MenuListItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .framerClickable(onClick = onClick)
            .clip(RoundedCornerShape(24.dp))
            .background(DarkGray)
            .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = White.copy(alpha = 0.8f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(18.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = White
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = White.copy(alpha = 0.6f)
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = White.copy(alpha = 0.3f),
            modifier = Modifier.size(22.dp)
        )
    }
}
