package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RoyalBlue500,
    onPrimary = Color.White,
    primaryContainer = RoyalBlue900,
    onPrimaryContainer = SkyLight,
    secondary = BlueAccent,
    onSecondary = Navy900,
    secondaryContainer = RoyalBlue800,
    onSecondaryContainer = SkyLight,
    tertiary = Gold500,
    onTertiary = Navy900,
    background = MidnightBgDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceCardDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceSubtleDark,
    onSurfaceVariant = TextSecondaryDark,
    error = Rose500,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = RoyalBlue900,
    onPrimary = Color.White,
    primaryContainer = SkyLight,
    onPrimaryContainer = RoyalBlue900,
    secondary = RoyalBlue500,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDBEAFE),
    onSecondaryContainer = RoyalBlue900,
    tertiary = Gold600,
    onTertiary = Color.White,
    background = SlateBgLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceCardLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceSubtleLight,
    onSurfaceVariant = TextSecondaryLight,
    error = Rose500,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep McKays brand colors consistent
    content: @Composable () -> Unit,
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
        typography = Typography,
        content = content
    )
}
