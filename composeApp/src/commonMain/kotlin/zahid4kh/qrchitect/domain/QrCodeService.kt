package zahid4kh.qrchitect.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageConfig
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel as ZXingErrorLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class QrCodeService {

    suspend fun generateQrCode(
        content: String,
        size: Int = 512,
        foregroundColor: Color = Color.Black,
        backgroundColor: Color = Color.White,
        errorCorrectionLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.M
    ): ImageBitmap = withContext(Dispatchers.IO) {
        val fgColor = java.awt.Color(
            (foregroundColor.red * 255).toInt(),
            (foregroundColor.green * 255).toInt(),
            (foregroundColor.blue * 255).toInt()
        )
        val bgColor = java.awt.Color(
            (backgroundColor.red * 255).toInt(),
            (backgroundColor.green * 255).toInt(),
            (backgroundColor.blue * 255).toInt()
        )

        val hints = mutableMapOf<EncodeHintType, Any>()
        hints[EncodeHintType.ERROR_CORRECTION] = when (errorCorrectionLevel) {
            ErrorCorrectionLevel.L -> ZXingErrorLevel.L
            ErrorCorrectionLevel.M -> ZXingErrorLevel.M
            ErrorCorrectionLevel.Q -> ZXingErrorLevel.Q
            ErrorCorrectionLevel.H -> ZXingErrorLevel.H
        }
        hints[EncodeHintType.MARGIN] = 1

        val bitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            size,
            size,
            hints
        )

        val config = MatrixToImageConfig(fgColor.rgb, bgColor.rgb)
        val qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config)

        qrImage.toComposeImageBitmap()
    }

    suspend fun generateQrCodeBytes(
        content: String,
        size: Int = 512,
        foregroundColor: Color = Color.Black,
        backgroundColor: Color = Color.White,
        errorCorrectionLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.M,
        format: String = "PNG"
    ): ByteArray = withContext(Dispatchers.IO) {
        val qrBitmap = generateQrCode(
            content, size, foregroundColor, backgroundColor, errorCorrectionLevel
        )

        val outputStream = ByteArrayOutputStream()
        val bufferedImage = qrBitmap.toAwtImage()
        ImageIO.write(bufferedImage, format, outputStream)
        outputStream.toByteArray()
    }
}