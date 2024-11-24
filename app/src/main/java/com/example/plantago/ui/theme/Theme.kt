package com.example.plantago.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun PlantaGoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

// Cores do tema claro
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimaryLight,
    onPrimary = Color.White,
    secondary = GreenSecondaryLight,
    onSecondary = Color.White,
    tertiary = YellowTertiaryLight,
    background = BackgroundLight,
    onBackground = Color(0xFF1B5E20),
    surface = Color.White,
    onSurface = Color(0xFF1B5E20)
)

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimaryDark,
    onPrimary = Color.Black,
    secondary = GreenSecondaryDark,
    onSecondary = Color.Black,
    tertiary = YellowTertiaryDark,
    background = BackgroundDark,
    onBackground = Color(0xFFE8F5E9),
    surface = Color(0xFF37474F),
    onSurface = Color(0xFFE8F5E9)
)
