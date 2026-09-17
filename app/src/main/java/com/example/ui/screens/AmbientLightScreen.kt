package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ui.components.framerClickable
import com.example.ui.theme.DarkGray
import com.example.ui.theme.White

data class AmbientPreset(val name: String, val color: Color, val description: String)

val PRESETS = listOf(
    AmbientPreset("Signature", Color(0xFFE0E0E0), "Clean, bright, standard"),
    AmbientPreset("Cyberpunk", Color(0xFF00E5FF), "High contrast cyan"),
    AmbientPreset("Late Night", Color(0xFF304FFE), "Deep relaxing blue"),
    AmbientPreset("Energize", Color(0xFFFF3D00), "Vibrant orange for focus"),
    AmbientPreset("Calm Forest", Color(0xFF00C853), "Soothing emerald green"),
    AmbientPreset("Neon Purple", Color(0xFFAA00FF), "Electric purple vibe")
)

@Composable
fun AmbientLightScreen(navController: NavController) {
    var selectedPreset by remember { mutableStateOf(PRESETS[0]) }
    var brightness by remember { mutableStateOf(80f) }

    val animatedColor by animateColorAsState(
        targetValue = selectedPreset.color,
        animationSpec = tween(500),
        label = "ambient_color"
    )

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
                text = "Ambient Lighting",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        // Visualizer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            animatedColor.copy(alpha = (brightness / 100f).coerceIn(0.1f, 1f)),
                            DarkGray
                        ),
                        radius = 400f
                    )
                )
                .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = White.copy(alpha = 0.8f),
                modifier = Modifier.size(48.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Brightness Control
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Intensity", color = White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text("${brightness.toInt()}%", color = White.copy(alpha = 0.6f), fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Slider(
                value = brightness,
                onValueChange = { brightness = it },
                valueRange = 10f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = White,
                    activeTrackColor = animatedColor,
                    inactiveTrackColor = DarkGray
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mood Presets",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(PRESETS) { preset ->
                PresetCard(
                    preset = preset,
                    isSelected = preset == selectedPreset,
                    onClick = { selectedPreset = preset }
                )
            }
        }
    }
}

@Composable
fun PresetCard(preset: AmbientPreset, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .framerClickable(onClick = onClick)
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) preset.color.copy(alpha = 0.15f) else DarkGray)
            .border(
                width = 2.dp,
                color = if (isSelected) preset.color else White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(preset.color)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = preset.name,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = preset.description,
            fontSize = 12.sp,
            color = White.copy(alpha = 0.6f),
            lineHeight = 16.sp
        )
    }
}
