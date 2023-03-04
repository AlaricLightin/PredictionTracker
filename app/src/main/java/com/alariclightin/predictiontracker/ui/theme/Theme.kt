package com.alariclightin.predictiontracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple200,
    secondary = Purple700,
    tertiary = Teal200
)

private val LightColorScheme = lightColorScheme(
    primary = Green800,
    secondary = Green800Dark,
    tertiary = Green800Light,
    onPrimary = Color.White,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

enum class CardColorType {
    Normal,
    RightAnswer,
    WrongAnswer,
    Expired
}

interface ColorsContainer {
    @Composable
    operator fun get(index: CardColorType): Color
}

val ColorScheme.cardColors: ColorsContainer
    @Composable
    get() = object : ColorsContainer {
        @Composable
        override fun get(index: CardColorType): Color = when(index) {
            CardColorType.RightAnswer -> LightGreen200
            CardColorType.WrongAnswer -> Red200
            CardColorType.Expired -> Grey300
            CardColorType.Normal -> background
        }
    }

@Composable
fun PredictionTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}