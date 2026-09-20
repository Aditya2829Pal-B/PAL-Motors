package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGray
import com.example.ui.theme.LightGray
import com.example.ui.theme.White
import com.example.ui.theme.iOSBlue
import com.example.ui.theme.iOSGreen
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

enum class TelemetryMetric(val label: String) {
    VOLTAGE("Voltage (V)"),
    MOTOR_TEMP("Motor Temp (°C)"),
    DUAL("Dual Telemetry")
}

@Composable
fun BatteryDiagnosticChart(modifier: Modifier = Modifier) {
    var selectedMetric by remember { mutableStateOf(TelemetryMetric.DUAL) }
    var isStreaming by remember { mutableStateOf(true) }

    // Window size for rolling real-time buffer
    val windowSize = 20

    // State holding current real-time voltage and motor temperature points
    var voltageEntries by remember {
        mutableStateOf(
            List(windowSize) { index ->
                val base = 398.0f + ((index - 10) * 0.25f) + (Random.nextFloat() * 1.2f - 0.6f)
                FloatEntry(index.toFloat(), base)
            }
        )
    }

    var tempEntries by remember {
        mutableStateOf(
            List(windowSize) { index ->
                val base = 71.0f + ((index - 10) * 0.18f) + (Random.nextFloat() * 0.8f - 0.4f)
                FloatEntry(index.toFloat(), base)
            }
        )
    }

    // Live tick simulation
    var tickCounter by remember { mutableStateOf(windowSize) }

    LaunchedEffect(isStreaming) {
        while (isStreaming) {
            delay(1500L) // Real-time pulse every 1.5 seconds

            val lastVoltage = voltageEntries.lastOrNull()?.y ?: 398.0f
            val lastTemp = tempEntries.lastOrNull()?.y ?: 71.0f

            // Realistic random walk with boundary pull
            val voltageDrift = (Random.nextFloat() * 1.6f - 0.8f) + (398.0f - lastVoltage) * 0.1f
            val newVoltage = (lastVoltage + voltageDrift).coerceIn(388.0f, 406.0f)

            val tempDrift = (Random.nextFloat() * 0.8f - 0.4f) + (72.0f - lastTemp) * 0.08f
            val newTemp = (lastTemp + tempDrift).coerceIn(64.0f, 82.0f)

            val nextX = tickCounter.toFloat()
            tickCounter += 1

            // Roll buffer forward
            val updatedVoltage = (voltageEntries.drop(1) + FloatEntry(nextX, newVoltage))
            val updatedTemp = (tempEntries.drop(1) + FloatEntry(nextX, newTemp))

            // Re-normalize X coordinates to 0..windowSize-1 so graph scrolls cleanly
            voltageEntries = updatedVoltage.mapIndexed { idx, entry -> FloatEntry(idx.toFloat(), entry.y) }
            tempEntries = updatedTemp.mapIndexed { idx, entry -> FloatEntry(idx.toFloat(), entry.y) }
        }
    }

    // Chart model depending on selected mode
    val chartModel = remember(selectedMetric, voltageEntries, tempEntries) {
        when (selectedMetric) {
            TelemetryMetric.VOLTAGE -> entryModelOf(voltageEntries)
            TelemetryMetric.MOTOR_TEMP -> entryModelOf(tempEntries)
            TelemetryMetric.DUAL -> entryModelOf(voltageEntries, tempEntries)
        }
    }

    // Latest live values
    val currentVoltage = voltageEntries.lastOrNull()?.y ?: 398.0f
    val currentTemp = tempEntries.lastOrNull()?.y ?: 71.0f

    val minVoltage = voltageEntries.minOfOrNull { it.y } ?: 390f
    val maxVoltage = voltageEntries.maxOfOrNull { it.y } ?: 405f
    val minTemp = tempEntries.minOfOrNull { it.y } ?: 65f
    val maxTemp = tempEntries.maxOfOrNull { it.y } ?: 80f

    // Live pulsing dot animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(DarkGray)
            .border(1.dp, White.copy(alpha = 0.05f), RoundedCornerShape(32.dp))
            .padding(20.dp)
            .animateContentSize()
    ) {
        // Card Header with LIVE indicator and Play/Pause control
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                if (isStreaming) iOSGreen.copy(alpha = pulseAlpha)
                                else Color.Gray
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isStreaming) "LIVE TELEMETRY" else "STREAM PAUSED",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = if (isStreaming) iOSGreen else White.copy(alpha = 0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Powertrain Diagnostics",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }

            // Stream controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        // Reset buffer with fresh randomized nominal values
                        val resetVolt = List(windowSize) { idx ->
                            FloatEntry(idx.toFloat(), 397.0f + Random.nextFloat() * 2f)
                        }
                        val resetTmp = List(windowSize) { idx ->
                            FloatEntry(idx.toFloat(), 70.0f + Random.nextFloat() * 2f)
                        }
                        voltageEntries = resetVolt
                        tempEntries = resetTmp
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Telemetry Buffer",
                        tint = White.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightGray)
                        .framerClickable { isStreaming = !isStreaming }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isStreaming) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isStreaming) "Pause Stream" else "Resume Stream",
                            tint = White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isStreaming) "Pause" else "Live",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Metric Readout Cards (Voltage & Motor Temperature)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DiagnosticMetricTile(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Bolt,
                iconColor = iOSGreen,
                title = "Battery Voltage",
                valueText = String.format("%.1f V", currentVoltage),
                rangeText = String.format("%.0f - %.0f V", minVoltage, maxVoltage),
                status = "Nominal 400V Pack",
                isSelected = selectedMetric == TelemetryMetric.VOLTAGE || selectedMetric == TelemetryMetric.DUAL,
                onClick = { selectedMetric = TelemetryMetric.VOLTAGE }
            )

            DiagnosticMetricTile(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Thermostat,
                iconColor = Color(0xFFFF9500),
                title = "Motor Temp",
                valueText = String.format("%.1f °C", currentTemp),
                rangeText = String.format("%.0f - %.0f °C", minTemp, maxTemp),
                status = "Coolant Loop Active",
                isSelected = selectedMetric == TelemetryMetric.MOTOR_TEMP || selectedMetric == TelemetryMetric.DUAL,
                onClick = { selectedMetric = TelemetryMetric.MOTOR_TEMP }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Metric Selector Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(LightGray)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TelemetryMetric.values().forEach { metric ->
                val isSelected = selectedMetric == metric
                val animatedBg by animateColorAsState(
                    targetValue = if (isSelected) DarkGray else Color.Transparent,
                    label = "tab_bg"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(animatedBg)
                        .framerClickable { selectedMetric = metric }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = metric.label,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) White else White.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Real-Time Graph using Vico
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF141416))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            ProvideChartStyle(chartStyle = m3ChartStyle()) {
                Chart(
                    chart = lineChart(),
                    model = chartModel,
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chart Legend & Window Timeline Annotation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectedMetric == TelemetryMetric.VOLTAGE || selectedMetric == TelemetryMetric.DUAL) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(iOSGreen)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Pack Voltage (V)",
                        fontSize = 11.sp,
                        color = White.copy(alpha = 0.7f)
                    )
                }

                if (selectedMetric == TelemetryMetric.DUAL) {
                    Spacer(modifier = Modifier.width(12.dp))
                }

                if (selectedMetric == TelemetryMetric.MOTOR_TEMP || selectedMetric == TelemetryMetric.DUAL) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF9500))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Motor Temp (°C)",
                        fontSize = 11.sp,
                        color = White.copy(alpha = 0.7f)
                    )
                }
            }

            Text(
                text = "Past 30s Real-Time Window",
                fontSize = 11.sp,
                color = White.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun DiagnosticMetricTile(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconColor: Color,
    title: String,
    valueText: String,
    rangeText: String,
    status: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .framerClickable(onClick = onClick)
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) LightGray else Color(0xFF161618))
            .border(
                width = 1.dp,
                color = if (isSelected) iconColor.copy(alpha = 0.5f) else White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(iconColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            Text(
                text = rangeText,
                fontSize = 10.sp,
                color = White.copy(alpha = 0.45f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            color = White.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = valueText,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = White
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = status,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = iconColor.copy(alpha = 0.85f)
        )
    }
}
