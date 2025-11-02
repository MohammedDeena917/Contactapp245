package com.example.contactapp2.ui.theme

import android.app.Activity // <<< ERROR 4 THEEK HO GAYA
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme // <-- Humne iska istemal band kar diya hai, lekin phir bhi rakhna accha hai
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme // <<< ERROR 1 THEEK HO GAYA
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme // <<< ERROR 2 THEEK HO GAYA
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Yeh aapki colors.kt file se aa rahe hain. Yakeen karein ki wahan yeh define hain.
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun ContactApp2Theme(
    // Humne Dark Mode ko yahan 'false' karke band kar diya hai
    darkTheme: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // darkTheme hamesha false hai, isliye yeh hamesha LightColorScheme hi dega
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // <<< ERROR 3 THEEK HO GAYA (yeh Type.kt se import hota hai)
        content = content
    )
}
