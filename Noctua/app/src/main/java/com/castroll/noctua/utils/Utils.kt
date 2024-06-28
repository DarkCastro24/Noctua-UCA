package com.castroll.noctua.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.util.Properties
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class PhoneNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0, 8) else text.text
        val formatted = buildString {
            trimmed.forEachIndexed { index, char ->
                if (index == 4) append(" ")
                append(char)
            }
        }
        return TransformedText(AnnotatedString(formatted), OffsetMapping.Identity)
    }
}

fun formatDayString(day: String): String {
    return day.lowercase().replaceFirstChar { it.uppercase() }
}

fun generatePasswordFromUsername(username: String): String {
    val prefix = "pdm"
    val firstFourChars = username.take(4)
    return "$prefix$firstFourChars"
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun generateQRCode(text: String): ImageBitmap? {
    return try {
        val size = 1024
        val bits = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, size, size)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).apply {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    setPixel(x, y, if (bits.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
        }
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun sendEmail(email: String, password: String) {
    val termsAndConditions = """
        La aplicación Noctua UCA está destinada exclusivamente para uso educativo dentro de la universidad. Al utilizar esta aplicación, usted acepta las siguientes condiciones:

        1. La aplicación obtendrá datos de su cuenta de Google, incluyendo su foto de perfil, nombre completo y dirección de correo electrónico.
        
        2. Los datos recopilados se utilizarán únicamente con fines educativos y para mejorar la experiencia del usuario dentro de la universidad.
        
        3. Usted se compromete a utilizar la aplicación de manera responsable y a no compartir sus credenciales con nadie.

        En caso de aceptar los términos y condiciones, sus credenciales para acceder a Noctua UCA serán las siguientes, es opcional si desea actualizarlas en la seccion edición de perfil
        
        Usuario: $email
        Contraseña: $password
    """.trimIndent()

    val properties = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    val session = Session.getInstance(properties, object : javax.mail.Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication("noctuauca@gmail.com", "pvls xymi wziz wzkk")
        }
    })

    try {
        val message = MimeMessage(session).apply {
            setFrom(InternetAddress("noctuauca@gmail.com"))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
            subject = "Creación de cuenta en Noctua UCA"
            setText(termsAndConditions)
        }

        Transport.send(message)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
