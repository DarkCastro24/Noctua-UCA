package com.castroll.noctua.ui.qr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.castroll.noctua.ui.theme.MyApplicationTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.castroll.noctua.data.local.UserViewModel
import com.castroll.noctua.utils.generateQRCode
import com.example.frontend_noctua_uca.ui.qr.QRViewModel
import com.castroll.noctua.R

@Composable
fun QRScreen(qrViewModel: QRViewModel = viewModel(), userViewModel: UserViewModel) {
    val user by userViewModel.user.observeAsState()
    val qrCodeText = user?.username?.substring(0, 8) ?: "00000000"
    val fullName = user?.name ?: "NAME1 NAME2 LASTNAME LASTNAME2"
    val nameParts = fullName.split(" ")
    val name = nameParts.take(2).joinToString(" ")
    val lastname = nameParts.drop(2).joinToString(" ")
    val qrCodeBitmap = generateQRCode(qrCodeText)
    val screenWidthPx = LocalContext.current.resources.displayMetrics.widthPixels
    val qrCodeSizeDp = with(LocalDensity.current) { (screenWidthPx * 0.7f).toDp() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.vid_fondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                qrCodeBitmap?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "QR Code",
                        modifier = Modifier.size(qrCodeSizeDp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_credencial),
                        contentDescription = "Credencial Icon",
                        tint = Color(0xFF002366), // Azul marino
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = qrCodeText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = lastname,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QRScreenPreview() {
    MyApplicationTheme {
        val userViewModel = UserViewModel()
        QRScreen(userViewModel = userViewModel)
    }
}

@Composable
fun Float.toDp(): Dp {
    return (this / LocalDensity.current.density).toDp()
}
