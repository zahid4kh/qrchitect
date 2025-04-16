package zahid4kh.qrchitect.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

object FileUtils {

    suspend fun saveImageToFile(
        image: ImageBitmap,
        defaultFileName: String,
        format: String = "png"
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val fileName = showSaveFileDialog(defaultFileName, format)
            if (fileName != null) {
                val file = File(fileName)
                val bufferedImage = image.toAwtImage()
                ImageIO.write(bufferedImage, format, file)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun showSaveFileDialog(
        defaultFileName: String,
        format: String
    ): String? = withContext(Dispatchers.IO) {
        return@withContext if (isMacOS()) {
            showMacFileDialog(defaultFileName, format)
        } else {
            showSwingFileDialog(defaultFileName, format)
        }
    }

    private fun showMacFileDialog(defaultFileName: String, format: String): String? {
        val dialog = FileDialog(null as Frame?, "Save QR Code", FileDialog.SAVE)
        dialog.file = "$defaultFileName.$format"
        dialog.isVisible = true

        return if (dialog.file != null) {
            val fileName = dialog.file
            val directory = dialog.directory

            var path = directory + fileName
            if (!path.lowercase().endsWith(".$format")) {
                path += ".$format"
            }
            path
        } else {
            null
        }
    }

    private fun showSwingFileDialog(defaultFileName: String, format: String): String? {
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Save QR Code"
        fileChooser.selectedFile = File("$defaultFileName.$format")

        val filter = FileNameExtensionFilter(
            format.uppercase() + " Files", format
        )
        fileChooser.fileFilter = filter

        val result = fileChooser.showSaveDialog(null)

        return if (result == JFileChooser.APPROVE_OPTION) {
            var path = fileChooser.selectedFile.absolutePath
            if (!path.lowercase().endsWith(".$format")) {
                path += ".$format"
            }
            path
        } else {
            null
        }
    }

    private fun isMacOS(): Boolean {
        val osName = System.getProperty("os.name").lowercase()
        return osName.contains("mac") || osName.contains("darwin")
    }
}