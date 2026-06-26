package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.example.ui.theme.iOSGreen
import com.example.ui.theme.White
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(navController: NavController) {
    var isLocked by remember { mutableStateOf(true) }

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
                        text = "My Model S",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "282 mi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = White.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "Parked",
                        fontSize = 16.sp,
                        color = White.copy(alpha = 0.6f)
                    )
                }
                
                // Profile Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = White.copy(alpha = 0.8f))
                }
            }
        }

        item {
            // Car Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentAlignment = Alignment.Center
            ) {
                // In a real app, use an Image composable with a 3D car render.
                Text(
                    "3D Car Model Render",
                    color = White.copy(alpha = 0.3f),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        item {
            // Quick Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                    contentDescription = "Lock",
                    tint = White,
                    modifier = Modifier.size(28.dp).clickable { isLocked = !isLocked }
                )
                Icon(
                    imageVector = Icons.Default.AcUnit, // fan
                    contentDescription = "Climate",
                    tint = White,
                    modifier = Modifier.size(28.dp).clickable { navController.navigate("climate") }
                )
                Icon(
                    imageVector = Icons.Default.ElectricBolt,
                    contentDescription = "Charge",
                    tint = White,
                    modifier = Modifier.size(28.dp)
                )
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = "Trunk",
                    tint = White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Menu Items
        item { MenuListItem(icon = Icons.Default.Settings, title = "Controls", onClick = { navController.navigate("settings") }) }
        item { MenuListItem(icon = Icons.Default.Thermostat, title = "Climate", subtitle = "Active - Interior 74°F", onClick = { navController.navigate("climate") }) }
        item { MenuListItem(icon = Icons.Default.LocationOn, title = "Location", subtitle = "123 Main St, Anytown CA", onClick = { navController.navigate("map") }) }
        item { MenuListItem(icon = Icons.Default.ArrowUpward, title = "Summon", onClick = { navController.navigate("map") }) }
        item { MenuListItem(icon = Icons.Default.Security, title = "Security", onClick = { navController.navigate("settings") }) }
        
        item {
            Spacer(modifier = Modifier.height(48.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "MODEL S",
                    fontSize = 14.sp,
                    letterSpacing = 4.sp,
                    color = White.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun MenuListItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = White.copy(alpha = 0.8f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 17.sp,
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
            modifier = Modifier.size(24.dp)
        )
    }
}
