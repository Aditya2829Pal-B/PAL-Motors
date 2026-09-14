package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
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
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.iOSGreen

@Composable
fun ProfilesScreen(navController: NavController, viewModel: DriverProfileViewModel = viewModel()) {
    val profiles by viewModel.profiles.collectAsState()
    val activeProfile by viewModel.activeProfile.collectAsState()

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
                text = "Driver Profiles",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(profiles) { profile ->
                ProfileCard(
                    profile = profile,
                    isActive = profile.id == activeProfile?.id,
                    onClick = { viewModel.switchProfile(profile.id) }
                )
            }
        }
    }
}

@Composable
fun ProfileCard(profile: DriverProfile, isActive: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .framerClickable(onClick = onClick)
            .clip(RoundedCornerShape(24.dp))
            .background(if (isActive) iOSBlue.copy(alpha = 0.1f) else DarkGray)
            .border(
                1.dp,
                if (isActive) iOSBlue else White.copy(alpha = 0.05f),
                RoundedCornerShape(24.dp)
            )
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isActive) iOSBlue else Color(0xFF2C2C2E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(28.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = profile.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Seat: ${profile.seatPosition.toInt()}% • Climate: ${profile.climateTemp.toInt()}°",
                fontSize = 13.sp,
                color = White.copy(alpha = 0.6f)
            )
        }
        
        if (isActive) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Active",
                tint = iOSBlue,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
