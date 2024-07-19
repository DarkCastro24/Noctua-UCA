package com.castroll.noctua.ui.firstuse

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.castroll.noctua.MainActivity
import com.castroll.noctua.R
import com.castroll.noctua.data.remote.repository.*
import com.castroll.noctua.data.local.UserViewModel
import com.castroll.noctua.di.MyApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class FirstUseActivity : ComponentActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    private val userViewModel: UserViewModel by lazy {
        (application as MyApp).viewModelProvider[UserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        val username = firebaseAuth.currentUser?.email

        setContent {
            FirstUseScreen(username = username, userViewModel = userViewModel) {
                goToMainActivity()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FirstUseScreen(username: String?, userViewModel: UserViewModel, onProfileUpdated: () -> Unit) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val selectedCarrera = remember { mutableStateOf<String?>(null) }
    val selectedMaterias = remember { mutableStateListOf<Materia>() }
    val selectedMateriasAprobadas = remember { mutableStateListOf<Materia>() }
    val showDialogMaterias = remember { mutableStateOf(false) }
    val showDialogMateriasAprobadas = remember { mutableStateOf(false) }
    val showDialogCarreras = remember { mutableStateOf(false) }
    val showAlertSinMaterias = remember { mutableStateOf(false) }
    val showSuccessDialog = remember { mutableStateOf(false) }
    val phone = remember { mutableStateOf("") }
    val email = remember { mutableStateOf(username ?: "") }
    val biography = remember { mutableStateOf("") }
    val hobbies = remember { mutableStateOf("") }
    val userType = userViewModel.user.value?.type ?: 1

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.padding(0.dp)
    ) {
        FirstUseContent(
            selectedCarrera = selectedCarrera,
            selectedMaterias = selectedMaterias,
            selectedMateriasAprobadas = selectedMateriasAprobadas,
            showDialogMaterias = showDialogMaterias,
            showDialogMateriasAprobadas = showDialogMateriasAprobadas,
            showDialogCarreras = showDialogCarreras,
            showAlertSinMaterias = showAlertSinMaterias,
            showSuccessDialog = showSuccessDialog,
            phone = phone,
            email = email,
            biography = biography,
            hobbies = hobbies,
            snackbarHostState = snackbarHostState,
            userViewModel = userViewModel,
            onProfileUpdated = onProfileUpdated,
            userType = userType
        )

        if (showAlertSinMaterias.value) {
            AlertaSinMaterias(onDismissRequest = { showAlertSinMaterias.value = false })
        }

        if (showSuccessDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    showSuccessDialog.value = false
                    onProfileUpdated()
                },
                confirmButton = {
                    Button(onClick = {
                        showSuccessDialog.value = false
                        onProfileUpdated()
                    }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_save),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("OK")
                    }
                },
                title = { Text("Información actualizada") },
                text = { Text("Sus datos han sido actualizados en su perfil, puede utilizar su cuenta") }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FirstUseContent(
    selectedCarrera: MutableState<String?>,
    selectedMaterias: SnapshotStateList<Materia>,
    selectedMateriasAprobadas: SnapshotStateList<Materia>,
    showDialogMaterias: MutableState<Boolean>,
    showDialogMateriasAprobadas: MutableState<Boolean>,
    showDialogCarreras: MutableState<Boolean>,
    showAlertSinMaterias: MutableState<Boolean>,
    showSuccessDialog: MutableState<Boolean>,
    phone: MutableState<String>,
    email: MutableState<String>,
    biography: MutableState<String>,
    hobbies: MutableState<String>,
    snackbarHostState: SnackbarHostState,
    userViewModel: UserViewModel,
    onProfileUpdated: () -> Unit,
    userType: Int
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val imageResources = listOf(
        R.drawable.img_fondo_login,
        R.drawable.img_fondo_login2,
        R.drawable.img_fondo_login3,
        R.drawable.img_fondo_login4
    )
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(5000)
            currentIndex = (currentIndex + 1) % imageResources.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = imageResources[currentIndex]),
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.imagen2222),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 8.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF001F3F))
                )
                Text(
                    text = "Noctua UCA",
                    color = Color(0xFF001F3F),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) } // Top space

            item {
                Text(
                    text = "¡BIENVENIDO!\n${userViewModel.user.value?.name ?: "INVITADO"},\n¿YA COMPLETASTE TU INFORMACIÓN PERSONAL?",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Light),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (userType != 0) {
                item {
                    OutlinedTextField(
                        value = selectedCarrera.value ?: "",
                        onValueChange = {},
                        label = { Text("Carrera seleccionada") },
                        placeholder = { Text("Seleccionar el botón para agregar carrera") },
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_carrera),
                                contentDescription = null,
                                tint = Color(0xFF001F3F),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF001F3F),
                            unfocusedBorderColor = Color(0xFF001F3F),
                            cursorColor = Color(0xFF001F3F)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (selectedCarrera.value != null) Color(0xFF001F3F) else Color.Transparent)
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Button(
                        onClick = { showDialogCarreras.value = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Seleccionar carrera", fontSize = 12.sp, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.icon_abajo),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    OutlinedTextField(
                        value = selectedMaterias.joinToString(", ") { it.nombre },
                        onValueChange = {},
                        label = { Text("Materias en curso") },
                        placeholder = {
                            if (selectedCarrera.value == null) {
                                Text("Seleccionar carrera primero")
                            } else {
                                Text("Seleccionar el botón para agregar materias")
                            }
                        },
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_materia1),
                                contentDescription = null,
                                tint = Color(0xFF001F3F),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF001F3F),
                            unfocusedBorderColor = Color(0xFF001F3F),
                            cursorColor = Color(0xFF001F3F)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (selectedMaterias.isNotEmpty()) Color(0xFF001F3F) else Color.Transparent)
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                if (selectedCarrera.value != null) {
                                    showDialogMaterias.value = true
                                } else {
                                    Toast.makeText(context, "Debe seleccionar una carrera primero", Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White),
                            modifier = Modifier
                                .weight(1.5f)
                                .padding(end = 4.dp)
                        ) {
                            Text("Agregar materias", fontSize = 12.sp, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.icon_add),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Button(
                            onClick = { selectedMaterias.clear() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            Text("Limpiar", fontSize = 12.sp, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.icon_basura),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    OutlinedTextField(
                        value = selectedMateriasAprobadas.joinToString(", ") { it.nombre },
                        onValueChange = {},
                        label = { Text("Materias aprobadas (opcional)") },
                        placeholder = {
                            if (selectedCarrera.value == null) {
                                Text("Seleccionar carrera primero")
                            } else {
                                Text("Seleccionar el botón para agregar materias aprobadas")
                            }
                        },
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_materia1),
                                contentDescription = null,
                                tint = Color(0xFF001F3F),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = Color(0xFF001F3F),
                            unfocusedBorderColor = Color(0xFF001F3F),
                            cursorColor = Color(0xFF001F3F)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (selectedMateriasAprobadas.isNotEmpty()) Color(0xFF001F3F) else Color.Transparent)
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                if (selectedCarrera.value != null) {
                                    showDialogMateriasAprobadas.value = true
                                } else {
                                    Toast.makeText(context, "Debe seleccionar una carrera primero", Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White),
                            modifier = Modifier
                                .weight(1.5f)
                                .padding(end = 4.dp)
                        ) {
                            Text("Agregar materias", fontSize = 12.sp, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.icon_add),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Button(
                            onClick = { selectedMateriasAprobadas.clear() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        ) {
                            Text("Limpiar", fontSize = 12.sp, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.icon_basura),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            item {
                OutlinedTextField(
                    value = phone.value,
                    onValueChange = {
                        if (it.length <= 8 && it.all { char -> char.isDigit() }) {
                            phone.value = it
                        }
                    },
                    label = { Text("Teléfono (opcional)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_phone_profile),
                            contentDescription = null,
                            tint = Color(0xFF001F3F),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF001F3F),
                        unfocusedBorderColor = Color(0xFF001F3F),
                        cursorColor = Color(0xFF001F3F)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Correo de contacto *") },
                    placeholder = { Text("correo@example.com") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_correo),
                            contentDescription = null,
                            tint = Color(0xFF001F3F),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF001F3F),
                        unfocusedBorderColor = Color(0xFF001F3F),
                        cursorColor = Color(0xFF001F3F)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                OutlinedTextField(
                    value = biography.value,
                    onValueChange = { biography.value = it },
                    label = { Text("Biografía *") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_bio_profile),
                            contentDescription = null,
                            tint = Color(0xFF001F3F),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF001F3F),
                        unfocusedBorderColor = Color(0xFF001F3F),
                        cursorColor = Color(0xFF001F3F)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                OutlinedTextField(
                    value = hobbies.value,
                    onValueChange = { hobbies.value = it },
                    label = { Text("Hobbies *") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_hobbies_profile),
                            contentDescription = null,
                            tint = Color(0xFF001F3F),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF001F3F),
                        unfocusedBorderColor = Color(0xFF001F3F),
                        cursorColor = Color(0xFF001F3F)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Button(
                    onClick = {
                        if (email.value.isEmpty() || biography.value.isEmpty() || hobbies.value.isEmpty()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Todos los campos deben estar completos")
                            }
                        } else {
                            coroutineScope.launch {
                                val currentSubjectsString = selectedMaterias.joinToString(", ") { it.nombre }
                                val approvedSubjectsString = selectedMateriasAprobadas.joinToString(", ") { it.nombre }

                                val success = userViewModel.updateUser(
                                    currentSubjects = if (userType == 0) "" else currentSubjectsString,
                                    allSubjects = if (userType == 0) "" else approvedSubjectsString,
                                    career = if (userType == 0) "" else selectedCarrera.value,
                                    phone = phone.value,
                                    email = email.value,
                                    biography = biography.value,
                                    hobbies = hobbies.value,
                                    visible = true
                                )
                                if (success) {
                                    showSuccessDialog.value = true
                                } else {
                                    snackbarHostState.showSnackbar("Error al actualizar el perfil")
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios", fontSize = 12.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.icon_save),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (showDialogMaterias.value) {
            val materias = when (selectedCarrera.value) {
                "Ingenieria Informatica" -> obtenerMateriasInformatica()
                "Ingenieria Electrica" -> obtenerMateriasElectrica()
                "Ingenieria Mecanica" -> obtenerMateriasMecanica()
                "Ingenieria Energetica" -> obtenerMateriasEnergetica()
                "Ingenieria en Alimentos" -> obtenerMateriasAlimentos()
                "Ingenieria Quimica" -> obtenerMateriasQuimica()
                "Ingenieria Civil" -> obtenerMateriasCivil()
                "Ingenieria Industrial" -> obtenerMateriasIndustrial()
                else -> emptyList()
            }
            if (materias.isEmpty()) {
                showAlertSinMaterias.value = true
            } else {
                MateriaSearchDialog(
                    materias = materias.filter { it !in selectedMaterias && it !in selectedMateriasAprobadas },
                    onMateriaSelected = { materia ->
                        selectedMaterias.add(materia)
                        showDialogMaterias.value = false
                    },
                    onDismissRequest = { showDialogMaterias.value = false }
                )
            }
        }

        if (showDialogMateriasAprobadas.value) {
            val materias = when (selectedCarrera.value) {
                "Ingenieria Informatica" -> obtenerMateriasInformatica()
                "Ingenieria Electrica" -> obtenerMateriasElectrica()
                "Ingenieria Mecanica" -> obtenerMateriasMecanica()
                "Ingenieria Energetica" -> obtenerMateriasEnergetica()
                "Ingenieria en Alimentos" -> obtenerMateriasAlimentos()
                "Ingenieria Quimica" -> obtenerMateriasQuimica()
                "Ingenieria Civil" -> obtenerMateriasCivil()
                "Ingenieria Industrial" -> obtenerMateriasIndustrial()
                else -> emptyList()
            }
            if (materias.isEmpty()) {
                showAlertSinMaterias.value = true
            } else {
                MateriaSearchDialog(
                    materias = materias.filter { it !in selectedMateriasAprobadas && it !in selectedMaterias },
                    onMateriaSelected = { materia ->
                        selectedMateriasAprobadas.add(materia)
                        showDialogMateriasAprobadas.value = false
                    },
                    onDismissRequest = { showDialogMateriasAprobadas.value = false }
                )
            }
        }

        if (showDialogCarreras.value) {
            CarreraSearchDialog(
                carreras = listaCarreras,
                onCarreraSelected = { carrera ->
                    selectedCarrera.value = carrera
                    selectedMaterias.clear()
                    selectedMateriasAprobadas.clear()
                    showDialogCarreras.value = false
                },
                onDismissRequest = { showDialogCarreras.value = false }
            )
        }
    }
}

@Composable
fun AlertaSinMaterias(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Materias no disponibles") },
        text = { Text("Aún no hay materias que mostrar para la carrera seleccionada.") },
        confirmButton = {
            Button(onClick = onDismissRequest, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)) {
                Text("OK")
            }
        }
    )
}

@Composable
fun MateriaSearchDialog(
    materias: List<Materia>,
    onMateriaSelected: (Materia) -> Unit,
    onDismissRequest: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMateria by remember { mutableStateOf<Materia?>(null) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Buscar Materia") },
        text = {
            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                val filteredMaterias =
                    materias.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
                LazyColumn {
                    items(filteredMaterias) { materia ->
                        val isSelected = materia == selectedMateria
                        TextButton(
                            onClick = {
                                onMateriaSelected(materia)
                                selectedMateria = materia
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = if (isSelected) Color(0xFF001F3F) else Color.Black),
                            modifier = Modifier.border(1.dp, if (isSelected) Color(0xFF001F3F) else Color.Transparent)
                        ) {
                            Text(materia.nombre)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismissRequest, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)) {
                Text("Cerrar")
            }
        }
    )
}

@Composable
fun CarreraSearchDialog(
    carreras: List<String>,
    onCarreraSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCarrera by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Buscar Carrera") },
        text = {
            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                val filteredCarreras =
                    carreras.filter { it.contains(searchQuery, ignoreCase = true) }
                LazyColumn {
                    items(filteredCarreras) { carrera ->
                        val isSelected = carrera == selectedCarrera
                        TextButton(
                            onClick = {
                                onCarreraSelected(carrera)
                                selectedCarrera = carrera
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = if (isSelected) Color(0xFF001F3F) else Color.Black),
                            modifier = Modifier.border(1.dp, if (isSelected) Color(0xFF001F3F) else Color.Transparent)
                        ) {
                            Text(carrera)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismissRequest, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)) {
                Text("Cerrar")
            }
        }
    )
}