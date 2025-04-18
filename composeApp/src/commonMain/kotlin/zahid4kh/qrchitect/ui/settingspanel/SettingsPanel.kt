package zahid4kh.qrchitect.ui.settingspanel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Moon
import compose.icons.feathericons.Sun
import kotlinx.coroutines.launch
import zahid4kh.qrchitect.data.AppSettings
import zahid4kh.qrchitect.data.SettingsService
import zahid4kh.qrchitect.theme.LocalThemeIsDark

@Composable
fun SettingsPanel(
    settingsService: SettingsService,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var isDark by LocalThemeIsDark.current

    LaunchedEffect(Unit) {
        val settings = settingsService.loadSettings()
        isDark = settings.isDarkTheme
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedCard(
                modifier = Modifier
                    .width(400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isDark) FeatherIcons.Moon else FeatherIcons.Sun,
                                contentDescription = "Theme Icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Dark Theme",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Switch(
                            checked = isDark,
                            onCheckedChange = { checked ->
                                isDark = checked
                                coroutineScope.launch {
                                    settingsService.saveSettings(AppSettings(isDarkTheme = checked))
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}