package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.components.framerClickable
import com.example.ui.theme.DarkGray
import com.example.ui.theme.LightGray
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.iOSGreen
import com.example.ui.theme.iOSRed
import kotlinx.coroutines.delay

@Composable
fun MapScreen(navController: NavController) {
    val context = LocalContext.current
    var summonState by remember { mutableStateOf<String?>(null) }
    var summonDistance by remember { mutableStateOf(25) }
    var showLocationCard by remember { mutableStateOf(false) }

    LaunchedEffect(summonState) {
        if (summonState != null) {
            summonDistance = 25
            while (summonDistance > 0 && summonState != null) {
                delay(800)
                summonDistance -= 2
            }
            if (summonDistance <= 0) {
                Toast.makeText(context, "Summon maneuver completed safely", Toast.LENGTH_SHORT).show()
                summonState = null
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            // High-Contrast Satellite/Nav Map Canvas
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E22)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(iOSBlue.copy(alpha = 0.2f))
                            .border(2.dp, iOSBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = "Car GPS Heading",
                            tint = White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "PAL Motors Model S",
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        "123 Main St, Beverly Hills, CA",
                        color = White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                }
            }

            // Top Controls (Back and Profile)
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
                        .size(44.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .framerClickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", tint = White)
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.65f))
                        .framerClickable { navController.navigate("profiles") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", tint = White)
                }
            }

            // Floating Location info card
            if (showLocationCard) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 80.dp, start = 24.dp, end = 24.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(DarkGray)
                        .border(1.dp, iOSBlue.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Current Vehicle GPS", fontWeight = FontWeight.Bold, color = White, fontSize = 14.sp)
                            IconButton(onClick = { showLocationCard = false }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = White.copy(alpha = 0.5f))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Accuracy: 1.1m (Dual-band GPS/GLONASS)", color = iOSGreen, fontSize = 12.sp)
                        Text("34.0736° N, 118.4004° W", color = White.copy(alpha = 0.6f), fontSize = 12.sp)
                    }
                }
            }

            // Locate Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .size(52.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkGray)
                    .border(1.dp, White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                    .framerClickable {
                        showLocationCard = !showLocationCard
                        Toast.makeText(context, "Vehicle GPS locked to 123 Main St", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Locate", tint = iOSBlue)
            }
        }

        // Summon Bottom Sheet Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Autonomous Summon", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = White)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        if (summonState != null) "Maneuvering vehicle ($summonDistance ft remaining)"
                        else "Vehicle will navigate obstacles autonomously",
                        fontSize = 13.sp,
                        color = if (summonState != null) iOSGreen else White.copy(alpha = 0.6f)
                    )
                }

                if (summonState != null) {
                    Button(
                        onClick = {
                            summonState = null
                            Toast.makeText(context, "Summon emergency stopped", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = iOSRed)
                    ) {
                        Text("STOP", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Go to Target
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(if (summonState == "target") iOSBlue else DarkGray)
                    .framerClickable {
                        summonState = "target"
                        Toast.makeText(context, "Summoning vehicle to your target location", Toast.LENGTH_SHORT).show()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NearMe, contentDescription = null, tint = White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Come to My Location", color = White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Directional summon buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (summonState == "forward") iOSGreen.copy(alpha = 0.2f) else DarkGray)
                        .border(1.dp, if (summonState == "forward") iOSGreen else White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                        .framerClickable {
                            summonState = "forward"
                            Toast.makeText(context, "Moving vehicle forward", Toast.LENGTH_SHORT).show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = "Forward", tint = if (summonState == "forward") iOSGreen else White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Forward", color = White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (summonState == "reverse") iOSGreen.copy(alpha = 0.2f) else DarkGray)
                        .border(1.dp, if (summonState == "reverse") iOSGreen else White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                        .framerClickable {
                            summonState = "reverse"
                            Toast.makeText(context, "Moving vehicle in reverse", Toast.LENGTH_SHORT).show()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = "Reverse", tint = if (summonState == "reverse") iOSGreen else White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reverse", color = White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
