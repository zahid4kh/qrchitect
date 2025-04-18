package zahid4kh.qrchitect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import zahid4kh.qrchitect.data.AppSettings
import zahid4kh.qrchitect.data.SettingsService
import zahid4kh.qrchitect.theme.AppTheme
import zahid4kh.qrchitect.theme.LocalThemeIsDark
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
    val settingsService = koinInject<SettingsService>()
    val coroutineScope = rememberCoroutineScope()
    var isDark by LocalThemeIsDark.current

    LaunchedEffect(Unit) {
        val settings = settingsService.loadSettings()
        isDark = settings.isDarkTheme
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        MainScreen(
            onThemeChanged = { newIsDark ->
                isDark = newIsDark
                coroutineScope.launch {
                    settingsService.saveSettings(AppSettings(isDarkTheme = newIsDark))
                }
            }
        )
    }
}