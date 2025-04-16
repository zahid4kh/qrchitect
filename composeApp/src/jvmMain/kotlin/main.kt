import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import zahid4kh.qrchitect.App
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import javax.swing.UIManager

fun main() {

    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    } catch (e: Exception) {
        e.printStackTrace()
    }

    application {
        Window(
            title = "QRchitect",
            state = rememberWindowState(width = 1000.dp, height = 700.dp),
            onCloseRequest = ::exitApplication,
        ) {
            window.minimumSize = Dimension(800, 600)
            DevelopmentEntryPoint {
                App()
            }
        }
    }
}