package zahid4kh.qrchitect.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class AppSettings(
    val isDarkTheme: Boolean = true
)

class SettingsService(private val json: Json) {
    private val settingsFolder = File(System.getProperty("user.home"), ".qrchitect")
    private val settingsFile = File(settingsFolder, "theme.json")

    suspend fun saveSettings(settings: AppSettings) = withContext(Dispatchers.IO) {
        try {
            if (!settingsFolder.exists()) {
                settingsFolder.mkdirs()
            }

            val jsonString = json.encodeToString(settings)
            settingsFile.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun loadSettings(): AppSettings = withContext(Dispatchers.IO) {
        try {
            if (settingsFile.exists()) {
                val jsonString = settingsFile.readText()
                json.decodeFromString<AppSettings>(jsonString)
            } else {
                AppSettings()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AppSettings()
        }
    }
}