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
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel as ZXingErrorLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import java.awt.BasicStroke
import java.awt.RenderingHints

class QrCodeService {

    suspend fun generateQrCode(
        content: String,
        size: Int = 512,
        foregroundColor: Color = Color.Black,
        backgroundColor: Color = Color.White,
        errorCorrectionLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.M,
        customization: QrCustomization? = null
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
        var qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config)

        if (customization != null) {
            qrImage = applyCustomization(qrImage, bitMatrix, customization, fgColor, bgColor)
        }

        qrImage.toComposeImageBitmap()
    }

    suspend fun generateQrCodeBytes(
        content: String,
        size: Int = 512,
        foregroundColor: Color = Color.Black,
        backgroundColor: Color = Color.White,
        errorCorrectionLevel: ErrorCorrectionLevel = ErrorCorrectionLevel.M,
        customization: QrCustomization? = null,
        format: String = "PNG"
    ): ByteArray = withContext(Dispatchers.IO) {
        val qrBitmap = generateQrCode(
            content, size, foregroundColor, backgroundColor, errorCorrectionLevel, customization
        )

        val outputStream = ByteArrayOutputStream()
        val bufferedImage = qrBitmap.toAwtImage()
        ImageIO.write(bufferedImage, format, outputStream)
        outputStream.toByteArray()
    }

    private fun applyCustomization(
        qrImage: BufferedImage,
        bitMatrix: BitMatrix,
        customization: QrCustomization,
        fgColor: java.awt.Color,
        bgColor: java.awt.Color
    ): BufferedImage {
        val width = qrImage.width
        val height = qrImage.height
        val cellSize = width / bitMatrix.width

        val customImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = customImage.createGraphics()

        g2d.color = bgColor
        g2d.fillRect(0, 0, width, height)

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        applyDotStyle(g2d, bitMatrix, cellSize, customization.dotStyle, fgColor)

        applyEyeStyle(g2d, bitMatrix, cellSize, customization.eyeStyle, fgColor)

        customization.frameStyle?.let {
            applyFrameStyle(g2d, width, height, it, fgColor)
        }

        customization.logoPath?.let {
            applyLogo(g2d, width, height, it)
        }

        g2d.dispose()
        return customImage
    }

    private fun applyDotStyle(
        g2d: Graphics2D,
        bitMatrix: BitMatrix,
        cellSize: Int,
        dotStyle: DotStyle,
        fgColor: java.awt.Color
    ) {
        g2d.color = fgColor

        val finderPatternSize = 7

        for (y in 0 until bitMatrix.height) {
            for (x in 0 until bitMatrix.width) {
                if (isInFinderPattern(x, y, bitMatrix.width, finderPatternSize)) {
                    continue
                }

                if (bitMatrix.get(x, y)) {
                    val xPos = x * cellSize
                    val yPos = y * cellSize

                    val inset = (cellSize * dotStyle.insetPercentage).toInt()
                    val actualSize = cellSize - (2 * inset)

                    when (dotStyle.type) {
                        DotType.SQUARE -> {
                            g2d.fillRect(xPos + inset, yPos + inset, actualSize, actualSize)
                        }
                        DotType.ROUNDED -> {
                            val radius = (dotStyle.cornerRadius * actualSize).toInt()
                            g2d.fillRoundRect(
                                xPos + inset, yPos + inset,
                                actualSize, actualSize,
                                radius, radius
                            )
                        }
                        DotType.CIRCLE -> {
                            g2d.fillOval(xPos + inset, yPos + inset, actualSize, actualSize)
                        }
                        DotType.DIAMOND -> {
                            val diamondPath = java.awt.Polygon().apply {
                                addPoint(xPos + inset + actualSize / 2, yPos + inset)
                                addPoint(xPos + inset + actualSize, yPos + inset + actualSize / 2)
                                addPoint(xPos + inset + actualSize / 2, yPos + inset + actualSize)
                                addPoint(xPos + inset, yPos + inset + actualSize / 2)
                            }
                            g2d.fill(diamondPath)
                        }
                        DotType.HEART -> {
                            g2d.fillRect(xPos + inset, yPos + inset, actualSize, actualSize)
                        }
                    }
                }
            }
        }
    }

    private fun applyEyeStyle(
        g2d: Graphics2D,
        bitMatrix: BitMatrix,
        cellSize: Int,
        eyeStyle: EyeStyle,
        defaultColor: java.awt.Color
    ) {
        val finderPatternSize = 7
        val innerColor = eyeStyle.innerColor?.let { java.awt.Color(it) } ?: defaultColor
        val outerColor = eyeStyle.outerColor?.let { java.awt.Color(it) } ?: defaultColor

        val positions = listOf(
            Pair(0, 0),
            Pair(bitMatrix.width - finderPatternSize, 0),
            Pair(0, bitMatrix.width - finderPatternSize)
        )

        for ((xOffset, yOffset) in positions) {
            val centerX = (xOffset + finderPatternSize / 2) * cellSize
            val centerY = (yOffset + finderPatternSize / 2) * cellSize
            val outerSize = finderPatternSize * cellSize
            val middleSize = 5 * cellSize
            val innerSize = 3 * cellSize

            g2d.color = outerColor

            when (eyeStyle.type) {
                EyeType.SQUARE -> {
                    g2d.fillRect(xOffset * cellSize, yOffset * cellSize, outerSize, outerSize)
                    g2d.color = bgColor
                    g2d.fillRect(
                        (xOffset + 1) * cellSize,
                        (yOffset + 1) * cellSize,
                        middleSize,
                        middleSize
                    )
                    g2d.color = innerColor
                    g2d.fillRect(
                        (xOffset + 2) * cellSize,
                        (yOffset + 2) * cellSize,
                        innerSize,
                        innerSize
                    )
                }
                EyeType.ROUNDED -> {
                    val outerRadius = outerSize / 4
                    val middleRadius = middleSize / 4
                    val innerRadius = innerSize / 4

                    g2d.fillRoundRect(
                        xOffset * cellSize,
                        yOffset * cellSize,
                        outerSize,
                        outerSize,
                        outerRadius,
                        outerRadius
                    )
                    g2d.color = bgColor
                    g2d.fillRoundRect(
                        (xOffset + 1) * cellSize,
                        (yOffset + 1) * cellSize,
                        middleSize,
                        middleSize,
                        middleRadius,
                        middleRadius
                    )
                    g2d.color = innerColor
                    g2d.fillRoundRect(
                        (xOffset + 2) * cellSize,
                        (yOffset + 2) * cellSize,
                        innerSize,
                        innerSize,
                        innerRadius,
                        innerRadius
                    )
                }
                EyeType.CIRCLE -> {
                    g2d.fillOval(
                        xOffset * cellSize,
                        yOffset * cellSize,
                        outerSize,
                        outerSize
                    )
                    g2d.color = bgColor
                    g2d.fillOval(
                        (xOffset + 1) * cellSize,
                        (yOffset + 1) * cellSize,
                        middleSize,
                        middleSize
                    )
                    g2d.color = innerColor
                    g2d.fillOval(
                        (xOffset + 2) * cellSize,
                        (yOffset + 2) * cellSize,
                        innerSize,
                        innerSize
                    )
                }
                EyeType.LEAF -> {
                    g2d.fillRect(
                        xOffset * cellSize,
                        yOffset * cellSize,
                        outerSize,
                        outerSize
                    )
                    g2d.color = bgColor
                    g2d.fillRect(
                        (xOffset + 1) * cellSize,
                        (yOffset + 1) * cellSize,
                        middleSize,
                        middleSize
                    )
                    g2d.color = innerColor
                    g2d.fillRect(
                        (xOffset + 2) * cellSize,
                        (yOffset + 2) * cellSize,
                        innerSize,
                        innerSize
                    )
                }
            }
        }
    }

    private fun applyFrameStyle(
        g2d: Graphics2D,
        width: Int,
        height: Int,
        frameStyle: FrameStyle,
        defaultColor: java.awt.Color
    ) {
        if (frameStyle.type == FrameType.NONE) return

        val frameColor = frameStyle.color?.let { java.awt.Color(it) } ?: defaultColor
        var frameWidth = frameStyle.width.toInt()
        val padding = frameStyle.padding.toInt()

        g2d.color = frameColor
        g2d.stroke = BasicStroke(frameWidth.toFloat())

        val x = padding
        val y = padding
        frameWidth = width - (2 * padding)
        val frameHeight = height - (2 * padding)

        when (frameStyle.type) {
            FrameType.SQUARE -> {
                g2d.drawRect(x, y, frameWidth, frameHeight)
            }
            FrameType.ROUNDED -> {
                val radius = (frameStyle.cornerRadius * frameWidth).toInt()
                g2d.drawRoundRect(x, y, frameWidth, frameHeight, radius, radius)
            }
            FrameType.CIRCLE -> {
                g2d.drawOval(x, y, frameWidth, frameHeight)
            }
            else -> {}
        }
    }

    private fun applyLogo(
        g2d: Graphics2D,
        width: Int,
        height: Int,
        logoPath: String
    ) {
        try {
            val logoImage = ImageIO.read(java.io.File(logoPath))
            if (logoImage != null) {
                val logoSize = width / 4
                val x = (width - logoSize) / 2
                val y = (height - logoSize) / 2

                g2d.color = java.awt.Color.WHITE
                g2d.fillRect(x - 2, y - 2, logoSize + 4, logoSize + 4)

                // Draw logo
                g2d.drawImage(
                    logoImage,
                    x, y,
                    logoSize, logoSize,
                    null
                )
            }
        } catch (e: Exception) {
            println("Failed to load logo: ${e.message}")
        }
    }

    private fun isInFinderPattern(
        x: Int,
        y: Int,
        size: Int,
        finderPatternSize: Int
    ): Boolean {
        val topLeft = x < finderPatternSize && y < finderPatternSize
        val topRight = x >= size - finderPatternSize && y < finderPatternSize
        val bottomLeft = x < finderPatternSize && y >= size - finderPatternSize

        return topLeft || topRight || bottomLeft
    }
}