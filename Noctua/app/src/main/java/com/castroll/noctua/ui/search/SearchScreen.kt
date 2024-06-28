package com.castroll.noctua.ui.search

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.castroll.noctua.R
import com.castroll.noctua.data.local.UserViewModel
import com.castroll.noctua.data.remote.model.User

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = viewModel(),
    userViewModel: UserViewModel
) {
    val showAlert = remember { mutableStateOf(false) }
    val alertMessage = remember { mutableStateOf("") }
    val users by searchViewModel.users.observeAsState(emptyList())
    val showUserDialog = remember { mutableStateOf(false) }
    val selectedUser = remember { mutableStateOf<User?>(null) }
    val searchText = remember { mutableStateOf("") }
    val currentPage by searchViewModel.currentPage.observeAsState(0)
    val totalPages by searchViewModel.totalPages.observeAsState(0)

    LaunchedEffect(Unit) {
        userViewModel.user.value?.username?.let { searchViewModel.fetchUsers(it) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.vid_fondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.padding(16.dp)) {
            SearchContent(
                searchText = searchText,
                onSearch = { query ->
                    if (query.isEmpty()) {
                        alertMessage.value = "Ingresa los indicios de tu búsqueda"
                        showAlert.value = true
                    } else {
                        searchViewModel.search(query)
                    }
                },
                onRestore = {
                    userViewModel.user.value?.username?.let { searchViewModel.restoreList(it) }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SearchUserContent(
                users = users,
                showUserDialog = showUserDialog,
                selectedUser = selectedUser
            )

            Spacer(modifier = Modifier.height(16.dp))

            PaginationControls(
                currentPage = currentPage,
                totalPages = totalPages,
                onNext = { searchViewModel.nextPage() },
                onPrevious = { searchViewModel.previousPage() }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = { showAlert.value = false },
            confirmButton = {
                Button(
                    onClick = { showAlert.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)
                ) {
                    Text("OK")
                }
            },
            title = { Text("Error de validación") },
            text = { Text(alertMessage.value) }
        )
    }

    if (showUserDialog.value && selectedUser.value != null) {
        UsersDetailsDialog(user = selectedUser.value!!) {
            showUserDialog.value = false
        }
    }
}

@Composable
fun SearchUserContent(
    users: List<User>,
    showUserDialog: MutableState<Boolean>,
    selectedUser: MutableState<User?>
) {
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
            .fillMaxHeight(0.8f)
    ) {
        items(users) { user ->
            UsersCard(user) {
                selectedUser.value = user
                showUserDialog.value = true
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    searchText: MutableState<String>,
    onSearch: (String) -> Unit,
    onRestore: () -> Unit
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = searchText.value,
                onValueChange = { query ->
                    searchText.value = query
                    if (query.isEmpty()) {
                        onRestore()
                    } else {
                        onSearch(query)
                    }
                },
                label = { Text("Buscar por nombre o carnet") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White.copy(alpha = 0.8f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.DarkGray,
                    focusedLabelColor = Color.DarkGray,
                    unfocusedLabelColor = Color.Gray
                ),
                textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light, color = Color.DarkGray),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_lupa),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}

@Composable
fun UsersCard(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.7f)
        )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(user.profilePhoto),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = user.name, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = Color.DarkGray)
                Text(text = "Email: ${user.username}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light), color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun UsersDetailsDialog(user: User, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val userType = if (user.type == 0) "Catedrático" else "Estudiante"

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Column {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)
                ) {
                    Text("Cerrar")
                }
            }
        },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(12.dp))
                Image(
                    painter = rememberAsyncImagePainter(user.profilePhoto),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        text = {
            BoxWithConstraints {
                val screenHeight = maxHeight
                Column(
                    modifier = Modifier
                        .heightIn(max = screenHeight * 0.75f)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    user.username?.let {
                        if (it.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_correo),
                                    contentDescription = "Correo",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Correo:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.DarkGray
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 24.dp)
                            ) {
                                Text(text = it, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light), color = Color.DarkGray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Enviar correo",
                                    tint = Color.DarkGray,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                                data = Uri.parse("mailto:$it")
                                            }
                                            context.startActivity(intent)
                                        }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    user.career?.let {
                        if (it.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_carrera),
                                    contentDescription = "Carrera",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Carrera:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.DarkGray
                                )
                            }
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_bio_profile),
                            contentDescription = "Tipo",
                            modifier = Modifier.size(20.dp),
                            tint = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Tipo:",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.DarkGray
                        )
                    }
                    Text(
                        text = userType,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    user.phone?.let {
                        if (it.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_phone_profile),
                                    contentDescription = "Teléfono",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Teléfono:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.DarkGray
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 24.dp)
                            ) {
                                Text(text = it, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light), color = Color.DarkGray)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_whatsapp),
                                    contentDescription = "Enviar mensaje",
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse("https://wa.me/$it")
                                            }
                                            context.startActivity(intent)
                                        }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    user.currentSubjects?.let {
                        if (it.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_bio_profile),
                                    contentDescription = "Asignaturas",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Asignaturas:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.DarkGray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            it.forEach { subject ->
                                Text(
                                    text = "* $subject",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(start = 24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                    user.biography?.let {
                        if (it.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_bio_profile),
                                    contentDescription = "Biografía",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Biografía:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.DarkGray
                                )
                            }
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    user.hobbies?.let {
                        if (it.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_hobbies_profile),
                                    contentDescription = "Hobbies",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Hobbies:",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.DarkGray
                                )
                            }
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                                color = Color.DarkGray,
                                modifier = Modifier.padding(start = 24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun PaginationControls(currentPage: Int, totalPages: Int, onNext: () -> Unit, onPrevious: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onPrevious, enabled = currentPage > 0) {
            Text("Anterior")
        }
        Text("Página ${currentPage + 1} de ${totalPages + 1}", style = MaterialTheme.typography.bodySmall)
        Button(onClick = onNext, enabled = currentPage < totalPages) {
            Text("Siguiente")
        }
    }
}


