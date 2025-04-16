package zahid4kh.qrchitect

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import qrchitect.composeapp.generated.resources.*
import zahid4kh.qrchitect.theme.AppTheme
import zahid4kh.qrchitect.theme.LocalThemeIsDark
import kotlinx.coroutines.isActive
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import zahid4kh.qrchitect.ui.MainScreen

@Composable
internal fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        AppContent()
    }
}


@Composable
private fun AppContent() = AppTheme {
    var isDark by LocalThemeIsDark.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MainScreen()
    }
}
