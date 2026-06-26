package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = iOSBlue,
    secondary = White,
    tertiary = iOSGreen,
    background = Black,
    surface = DarkGray,
    surfaceVariant = LightGray,
    onPrimary = White,
    onSecondary = Black,
    onBackground = White,
    onSurface = White
  )

private val LightColorScheme =
  lightColorScheme(
    primary = iOSBlue,
    secondary = Black,
    tertiary = iOSGreen,
    background = Color(0xFFF2F2F7),
    surface = White,
    surfaceVariant = Color(0xFFE5E5EA),
    onPrimary = White,
    onSecondary = White,
    onBackground = Black,
    onSurface = Black
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark theme for SLEEK EV aesthetic
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
