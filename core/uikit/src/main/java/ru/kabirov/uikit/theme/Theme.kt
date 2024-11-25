package ru.kabirov.uikit.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme =
    darkColorScheme(
        primary = PastelOrange,
        secondary = CarminePink,
        tertiary = Pink80,
        onPrimary = Color.Black,
        onSurface = Color.White,
        errorContainer = ErrorContainerColor,
        surfaceContainerHighest = DarkSurfaceContainerHighest
    )

private val LightColorScheme =
    lightColorScheme(
        primary = PastelOrange,
        secondary = CarminePink,
        tertiary = Pink40,
        // Other default colors to override
        background = Cultured,
//        surface = Color(0xFFFFFBFE),
        onPrimary = Color.Black,
//        onSecondary = Color.White,
//        onTertiary = Color.White,
//        onBackground = Color(0xFF1C1B1F),
        onSurface = Color.Black,
        errorContainer = ErrorContainerColor,
        surfaceContainerHighest = Color.White,
    )

@Composable
fun IpSearcherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        when {
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

object IpSearcherTheme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes
}