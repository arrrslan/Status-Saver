package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MonochromeDarkColorScheme = darkColorScheme(
    primary = MonoPrimary,
    onPrimary = MonoBackground,
    primaryContainer = MonoSurfaceHighlight,
    onPrimaryContainer = Color.White,
    secondary = MonoSecondary,
    onSecondary = MonoBackground,
    secondaryContainer = MonoSurfaceVariant,
    onSecondaryContainer = Color.White,
    tertiary = MonoTertiary,
    onTertiary = MonoBackground,
    background = MonoBackground,
    onBackground = MonoTextPrimary,
    surface = MonoSurface,
    onSurface = MonoTextPrimary,
    surfaceVariant = MonoSurfaceVariant,
    onSurfaceVariant = MonoTextSecondary,
    outline = MonoSurfaceBorder,
    outlineVariant = MonoSurfaceHighlight,
    error = ErrorRed,
    onError = Color.White
)

private val MonochromeLightColorScheme = lightColorScheme(
    primary = Color(0xFF18181B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE4E4E7),
    onPrimaryContainer = Color(0xFF18181B),
    secondary = Color(0xFF27272A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF4F4F5),
    onSecondaryContainer = Color(0xFF18181B),
    tertiary = Color(0xFF3F3F46),
    onTertiary = Color.White,
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF09090B),
    surface = Color.White,
    onSurface = Color(0xFF09090B),
    surfaceVariant = Color(0xFFF4F4F5),
    onSurfaceVariant = Color(0xFF71717A),
    outline = Color(0xFFD4D4D8),
    outlineVariant = Color(0xFFE4E4E7),
    error = ErrorRed,
    onError = Color.White
)

val androidx.compose.material3.ColorScheme.surfaceHighlight: Color
    get() = outlineVariant

@Composable
fun DualStatusSaverTheme(
    darkTheme: Boolean = true, // Default to Navy Slate Dark as selected by user
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MonochromeDarkColorScheme else MonochromeLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !darkTheme
                controller.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
