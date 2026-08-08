package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import kotlin.random.Random
import com.example.ui.theme.White
import com.example.ui.theme.DarkGray
import com.example.ui.theme.iOSGreen
import com.patrykandpatrick.vico.core.entry.FloatEntry

@Composable
fun BatteryDiagnosticChart() {
    val entries = remember {
        val list = mutableListOf<FloatEntry>()
        var value = 85f
        for (i in 0..60 step 5) { // every 5 mins for last hour
            list.add(FloatEntry(i.toFloat(), value))
            value -= Random.nextFloat() * 1.5f
        }
        entryModelOf(list)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Battery Telemetry (Last Hour)",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
            ProvideChartStyle(chartStyle = m3ChartStyle()) {
                Chart(
                    chart = lineChart(),
                    model = entries,
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(),
                )
            }
        }
    }
}
