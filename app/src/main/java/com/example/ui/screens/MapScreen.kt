package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.theme.DarkGray
import com.example.ui.theme.White

@Composable
fun MapScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            // Simulated Map Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF2C2C2E)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Offline Map Data Loaded",
                    color = White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Medium
                )
            }
            
            // Top Controls (Back button and Profile)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = White)
                }
                
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = White)
                }
            }

            // Locate Button
            FloatingActionButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                containerColor = DarkGray,
                contentColor = White
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Locate")
            }
        }
        
        // Summon Bottom Sheet
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(24.dp)
        ) {
            Text("Summon", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Press and hold to move vehicle", fontSize = 14.sp, color = White.copy(alpha = 0.6f))
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGray),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Go to Target", color = White, fontWeight = FontWeight.SemiBold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f).height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Forward", tint = White)
                }
                
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f).height(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkGray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ArrowDownward, contentDescription = "Backward", tint = White)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
