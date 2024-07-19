package com.castroll.noctua.ui.profile

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.castroll.noctua.R
import com.castroll.noctua.data.local.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    userViewModel: UserViewModel,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val user by userViewModel.user.observeAsState()

    val email = remember { mutableStateOf(user?.email ?: "No disponible") }
    val phone = remember { mutableStateOf(user?.phone ?: "No disponible") }
    val biography = remember { mutableStateOf(user?.biography ?: "No disponible") }
    val hobbies = remember { mutableStateOf(user?.hobbies ?: "No disponible") }
    val career = remember { mutableStateOf(user?.career ?: "No disponible") }
    val visible = remember { mutableStateOf(user?.visible ?: "true") }

    val showAlert = remember { mutableStateOf(false) }
    val alertMessage = remember { mutableStateOf("") }
    val showChangePasswordDialog = remember { mutableStateOf(false) }

    val outfitRegular = FontFamily(Font(R.font.outfitregular))

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondoooooo222),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF002366))
                        .shadow(4.dp, CircleShape)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(user?.profilePhoto ?: ""),
                        contentDescription = "User Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            if (email.value.isBlank() || phone.value.isBlank() || biography.value.isBlank() || hobbies.value.isBlank() || (user?.type != 0 && career.value.isBlank())) {
                                alertMessage.value = "Todos los campos deben estar llenos."
                                showAlert.value = true
                            } else {
                                user?.let {
                                    profileViewModel.updateUserProfile(
                                        it._id,
                                        email.value,
                                        phone.value,
                                        biography.value,
                                        hobbies.value,
                                        if (user?.type != 0) career.value else "",
                                        visible.value,
                                        context
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002366)), // Navy Blue color
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(text = "Editar perfil", color = Color.White, fontFamily = outfitRegular)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(R.drawable.icon_pencil),
                            contentDescription = "Edit Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Button(
                        onClick = { showChangePasswordDialog.value = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF002366)), // Navy Blue color
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Text(text = "Cambiar contraseña", color = Color.White, fontFamily = outfitRegular)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(R.drawable.icon_contrasena),
                            contentDescription = "Password Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                ProfileInfoField(label = "Nombre", value = user?.name ?: "No disponible", iconRes = R.drawable.icon_name_profile, editable = false, fontFamily = outfitRegular)
                ProfileInfoField(label = "Correo", value = email.value, iconRes = R.drawable.icon_correo_profile, onValueChange = { email.value = it }, fontFamily = outfitRegular)
                ProfileInfoField(label = "Teléfono", value = phone.value, iconRes = R.drawable.icon_phone_profile, onValueChange = { phone.value = it }, fontFamily = outfitRegular)
                ProfileInfoField(label = "Biografía", value = biography.value, iconRes = R.drawable.icon_bio_profile, onValueChange = { biography.value = it }, fontFamily = outfitRegular)
                ProfileInfoField(label = "Hobbies", value = hobbies.value, iconRes = R.drawable.icon_hobbies_profile, onValueChange = { hobbies.value = it }, fontFamily = outfitRegular)

                if (user?.type != 0) {
                    ProfileInfoField(label = "Carrera", value = career.value, iconRes = R.drawable.icon_carreer_profile, onValueChange = { career.value = it }, editable = user?.type != 1, fontFamily = outfitRegular)
                    ProfileInfoField(
                        label = "Materias en curso",
                        value = if (user?.currentSubjects.isNullOrEmpty()) "No hay materias agregadas" else user?.currentSubjects?.joinToString(", ") ?: "",
                        iconRes = R.drawable.icon_materias_profile,
                        editable = false,
                        fontFamily = outfitRegular
                    )
                    ProfileInfoField(
                        label = "Materias aprobadas",
                        value = if (user?.allSubjects.isNullOrEmpty()) "No hay materias agregadas" else user?.allSubjects?.joinToString(", ") ?: "",
                        iconRes = R.drawable.icon_carrera,
                        editable = false,
                        fontFamily = outfitRegular
                    )
                }

                if (user?.type == 0) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.icon_carreer_profile),
                                    contentDescription = null,
                                    tint = Color(0xFF002366), // Navy Blue color
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Visibilidad",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = Color(0xFF002366), // Navy Blue color
                                        fontFamily = outfitRegular
                                    ),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = visible.value == "true",
                                    onCheckedChange = { visible.value = "true" }
                                )
                                Text("Visible", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF002366), fontFamily = outfitRegular)) // Navy Blue color
                                Checkbox(
                                    checked = visible.value == "false",
                                    onCheckedChange = { visible.value = "false" }
                                )
                                Text("Oculto", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF002366), fontFamily = outfitRegular)) // Navy Blue color
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = { showAlert.value = false },
            confirmButton = {
                Button(onClick = { showAlert.value = false }) {
                    Text("OK", fontFamily = outfitRegular)
                }
            },
            title = { Text("Error de validación", fontFamily = outfitRegular) },
            text = { Text(alertMessage.value, fontFamily = outfitRegular) }
        )
    }

    if (showChangePasswordDialog.value) {
        ChangePasswordDialog(
            userViewModel = userViewModel,
            onDismiss = { showChangePasswordDialog.value = false },
            fontFamily = outfitRegular
        )
    }
}

@Composable
fun ChangePasswordDialog(userViewModel: UserViewModel, onDismiss: () -> Unit, fontFamily: FontFamily) {
    val currentPassword = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val context = LocalContext.current

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                if (newPassword.value == confirmPassword.value) {
                    (context as ComponentActivity).lifecycleScope.launch {
                        val errorMessage = userViewModel.updatePassword(currentPassword.value, newPassword.value)
                        showToast(errorMessage ?: "Contraseña actualizada correctamente")
                    }
                } else {
                    showToast("Las contraseñas no coinciden")
                }
            }) {
                Text("Actualizar contraseña", fontFamily = fontFamily)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar", fontFamily = fontFamily)
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF002366), // Navy Blue color
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cambiar contraseña", fontFamily = fontFamily)
            }
        },
        text = {
            Column {
                TextField(
                    value = currentPassword.value,
                    onValueChange = { currentPassword.value = it },
                    label = { Text("Contraseña actual", fontSize = 14.sp, fontFamily = fontFamily) }, // Placeholder más pequeño
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp)) // Más separación entre TextFields
                TextField(
                    value = newPassword.value,
                    onValueChange = { newPassword.value = it },
                    label = { Text("Nueva contraseña", fontSize = 14.sp, fontFamily = fontFamily) }, // Placeholder más pequeño
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp)) // Más separación entre TextFields
                TextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    label = { Text("Confirmar nueva contraseña", fontSize = 14.sp, fontFamily = fontFamily) }, // Placeholder más pequeño
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

@Composable
fun ProfileInfoField(label: String, value: String, iconRes: Int, editable: Boolean = true, onValueChange: (String) -> Unit = {}, fontFamily: FontFamily) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = Color(0xFF002366), // Navy Blue color
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color(0xFF002366), // Navy Blue color
                        fontFamily = fontFamily
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (editable) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Normal, fontFamily = fontFamily),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = fontFamily
                    )
                )
            }
        }
    }
}
