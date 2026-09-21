package kr.playax.novel.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Ink = Color(0xFF1A1510)
private val Paper = Color(0xFFF2E8D5)
private val Ember = Color(0xFFB33A1A)
private val Jade = Color(0xFF1F5C45)
private val Mist = Color(0xFFC9BFAE)

private val LightColors = lightColorScheme(
    primary = Jade,
    onPrimary = Paper,
    secondary = Ember,
    onSecondary = Paper,
    background = Paper,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Mist,
    onSurfaceVariant = Ink,
)

private val DarkColors = darkColorScheme(
    primary = Jade,
    onPrimary = Paper,
    secondary = Ember,
    onSecondary = Paper,
    background = Ink,
    onBackground = Paper,
    surface = Ink,
    onSurface = Paper,
)

@Composable
fun PlayAXNovelTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
