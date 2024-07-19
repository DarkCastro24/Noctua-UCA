package com.castroll.noctua.ui.signout

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.castroll.noctua.R
import com.castroll.noctua.ui.login.LoginActivity

@Composable
fun SignOutScreen(
    context: Context,
    signOutViewModel: SignOutViewModel = viewModel()
) {
    val text by signOutViewModel.text.observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondoooooo222),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = text ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    LogoutButton(context, signOutViewModel)
                }
            }
        }
    }
}

@Composable
fun LogoutButton(context: Context, signOutViewModel: SignOutViewModel) {
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val showConfirmDialog = remember { mutableStateOf(false) }

    Button(
        onClick = {
            showConfirmDialog.value = true
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(56.dp)
    ) {
        Text(text = "Cerrar sesión", fontSize = 18.sp)
    }

    if (showConfirmDialog.value) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog.value = false },
            confirmButton = {
                Button(
                    onClick = {
                        try {
                            //firebaseAuth.signOut()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            errorMessage.value = "Logout failed: ${e.message}"
                        }
                        showConfirmDialog.value = false
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Sí", fontSize = 18.sp)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmDialog.value = false },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("No", fontSize = 18.sp)
                }
            },
            title = { Text("Confirmar cierre de sesión", fontSize = 18.sp) },
            text = { Text("¿Está seguro de que desea cerrar sesión?", fontSize = 18.sp) }
        )
    }

    errorMessage.value?.let {
        Text(text = it, color = MaterialTheme.colorScheme.error)
    }
}
