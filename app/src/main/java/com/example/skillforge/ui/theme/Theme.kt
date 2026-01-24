package com.example.skillforge.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Crimson,
    onPrimary = SurfaceLight,
    primaryContainer = CrimsonLight,
    onPrimaryContainer = SurfaceDark,
    secondary = Amber,
    onSecondary = SurfaceDark,
    secondaryContainer = AmberLight,
    onSecondaryContainer = SurfaceDark,
    tertiary = Charcoal,
    onTertiary = SurfaceLight,
    tertiaryContainer = CharcoalLight,
    onTertiaryContainer = SurfaceLight,
    background = SurfaceLight,
    onBackground = SurfaceDark,
    surface = SurfaceLight,
    onSurface = SurfaceDark
)

private val DarkColorScheme = darkColorScheme(
    primary = CrimsonDark,
    onPrimary = SurfaceDark,
    primaryContainer = Crimson,
    onPrimaryContainer = SurfaceLight,
    secondary = AmberDark,
    onSecondary = SurfaceDark,
    secondaryContainer = Amber,
    onSecondaryContainer = SurfaceDark,
    tertiary = CharcoalDark,
    onTertiary = SurfaceDark,
    tertiaryContainer = Charcoal,
    onTertiaryContainer = SurfaceLight,
    background = SurfaceDark,
    onBackground = SurfaceLight,
    surface = SurfaceDark,
    onSurface = SurfaceLight
)

@Composable
fun SkillForgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}