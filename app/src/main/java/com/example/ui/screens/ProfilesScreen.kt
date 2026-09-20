package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AirlineSeatReclineExtra
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.data.DriverProfile
import com.example.data.DriverProfileViewModel
import com.example.ui.components.framerClickable
import com.example.ui.theme.DarkGray
import com.example.ui.theme.LightGray
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.iOSGreen
import com.example.ui.theme.iOSRed

@Composable
fun ProfilesScreen(navController: NavController, viewModel: DriverProfileViewModel = viewModel()) {
    val profiles by viewModel.profiles.collectAsState()
    val activeProfile by viewModel.activeProfile.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingProfileId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 48.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                Column {
                    Text(
                        text = "Driver Profiles",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Text(
                        text = "Persisted in Room DB",
                        fontSize = 12.sp,
                        color = White.copy(alpha = 0.5f)
                    )
                }
            }

            IconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(iOSBlue)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Profile", tint = White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(profiles, key = { it.id }) { profile ->
                val isActive = profile.id == activeProfile?.id
                val isEditing = editingProfileId == profile.id

                ProfileCardEnhanced(
                    profile = profile,
                    isActive = isActive,
                    isEditing = isEditing,
                    onSelect = { viewModel.switchProfile(profile.id) },
                    onToggleEdit = {
                        editingProfileId = if (isEditing) null else profile.id
                    },
                    onUpdate = { updated ->
                        viewModel.updateProfile(updated)
                    },
                    onDelete = {
                        viewModel.deleteProfile(profile.id)
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        var newName by remember { mutableStateOf("") }
        var seatPos by remember { mutableStateOf(50f) }
        var mirrorTilt by remember { mutableStateOf(50f) }
        var climateTemp by remember { mutableStateOf(72f) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("New Driver Profile", color = White, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Driver Name", color = White.copy(alpha = 0.6f)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedBorderColor = iOSBlue,
                            unfocusedBorderColor = White.copy(alpha = 0.2f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Seat Position: ${seatPos.toInt()}%", fontSize = 13.sp, color = White.copy(alpha = 0.7f))
                    Slider(
                        value = seatPos,
                        onValueChange = { seatPos = it },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(thumbColor = iOSBlue, activeTrackColor = iOSBlue)
                    )

                    Text("Climate Preference: ${climateTemp.toInt()}°F", fontSize = 13.sp, color = White.copy(alpha = 0.7f))
                    Slider(
                        value = climateTemp,
                        onValueChange = { climateTemp = it },
                        valueRange = 60f..85f,
                        colors = SliderDefaults.colors(thumbColor = iOSGreen, activeTrackColor = iOSGreen)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.addProfile(newName.trim(), seatPos, mirrorTilt, climateTemp)
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Save Profile", color = iOSBlue, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = White.copy(alpha = 0.6f))
                }
            },
            containerColor = DarkGray
        )
    }
}

@Composable
fun ProfileCardEnhanced(
    profile: DriverProfile,
    isActive: Boolean,
    isEditing: Boolean,
    onSelect: () -> Unit,
    onToggleEdit: () -> Unit,
    onUpdate: (DriverProfile) -> Unit,
    onDelete: () -> Unit
) {
    var seat by remember(profile.seatPosition) { mutableStateOf(profile.seatPosition) }
    var mirror by remember(profile.mirrorTilt) { mutableStateOf(profile.mirrorTilt) }
    var climate by remember(profile.climateTemp) { mutableStateOf(profile.climateTemp) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(if (isActive) iOSBlue.copy(alpha = 0.12f) else DarkGray)
            .border(
                1.dp,
                if (isActive) iOSBlue else White.copy(alpha = 0.05f),
                RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (isActive) iOSBlue else Color(0xFF2C2C2E))
                    .framerClickable(onClick = onSelect),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .framerClickable(onClick = onSelect)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    if (isActive) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ACTIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = iOSBlue
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Seat: ${seat.toInt()}% • Mirrors: ${mirror.toInt()}% • Climate: ${climate.toInt()}°F",
                    fontSize = 13.sp,
                    color = White.copy(alpha = 0.6f)
                )
            }

            IconButton(onClick = onToggleEdit) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Adjust Settings",
                    tint = if (isEditing) iOSBlue else White.copy(alpha = 0.5f),
                    modifier = Modifier.size(22.dp)
                )
            }

            if (isActive) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Active",
                    tint = iOSBlue,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        // Expanded editor for adjusting and persisting seat, mirror, climate
        if (isEditing) {
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = White.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(16.dp))

            Text("Seat Position: ${seat.toInt()}%", fontSize = 12.sp, color = White.copy(alpha = 0.8f))
            Slider(
                value = seat,
                onValueChange = {
                    seat = it
                    onUpdate(profile.copy(seatPosition = it))
                },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(thumbColor = iOSBlue, activeTrackColor = iOSBlue)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Side Mirror Tilt: ${mirror.toInt()}%", fontSize = 12.sp, color = White.copy(alpha = 0.8f))
            Slider(
                value = mirror,
                onValueChange = {
                    mirror = it
                    onUpdate(profile.copy(mirrorTilt = it))
                },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(thumbColor = iOSBlue, activeTrackColor = iOSBlue)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Climate Comfort: ${climate.toInt()}°F", fontSize = 12.sp, color = White.copy(alpha = 0.8f))
            Slider(
                value = climate,
                onValueChange = {
                    climate = it
                    onUpdate(profile.copy(climateTemp = it))
                },
                valueRange = 60f..85f,
                colors = SliderDefaults.colors(thumbColor = iOSGreen, activeTrackColor = iOSGreen)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isActive) {
                    TextButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = iOSRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete Profile", color = iOSRed, fontSize = 13.sp)
                    }
                } else {
                    Text("Current Active Driver", fontSize = 12.sp, color = White.copy(alpha = 0.4f))
                }

                Button(
                    onClick = onToggleEdit,
                    colors = ButtonDefaults.buttonColors(containerColor = LightGray)
                ) {
                    Text("Done", color = White, fontSize = 13.sp)
                }
            }
        }
    }
}
