package com.ai.venice.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VeniceColorScheme = darkColorScheme(
    primary = VeniceCyan,
    onPrimary = VeniceVoid,
    primaryContainer = VeniceSurfaceContainer,
    onPrimaryContainer = VeniceCyan,
    secondary = VeniceBlue,
    onSecondary = VeniceVoid,
    secondaryContainer = VeniceSurfaceVariant,
    onSecondaryContainer = VeniceCyan,
    tertiary = VeniceCoral,
    onTertiary = VeniceVoid,
    background = VeniceVoid,
    onBackground = TextPrimary,
    surface = VeniceSurface,
    onSurface = TextPrimary,
    surfaceVariant = VeniceSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = VeniceBorder,
    outlineVariant = VeniceBorderLight
)

@Composable
fun VeniceAITheme(
    darkTheme: Boolean = true, // Venice AI is stealth dark-first
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = VeniceColorScheme,
        content = content
    )
}
