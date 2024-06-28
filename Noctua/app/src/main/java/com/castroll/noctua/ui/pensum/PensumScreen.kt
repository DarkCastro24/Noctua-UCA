package com.castroll.noctua.ui.pensum

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.castroll.noctua.data.local.UserViewModel
import com.castroll.noctua.data.remote.repository.*
import com.castroll.noctua.utils.showToast
import com.castroll.noctua.R

@Composable
fun PensumScreen(userViewModel: UserViewModel) {
    PensumContent(userViewModel = userViewModel)
}

@Composable
fun PensumContent(userViewModel: UserViewModel) {
    val user by userViewModel.user.observeAsState()
    val context = LocalContext.current

    val showCurrentSubjectsDialog = remember { mutableStateOf(false) }
    val showTotalSubjectsDialog = remember { mutableStateOf(false) }
    val showAddSubjectsDialog = remember { mutableStateOf(false) }
    val subjectToRemove = remember { mutableStateOf<String?>(null) }
    val subjectToComplete = remember { mutableStateOf<String?>(null) }
    val totalSubjectToRemove = remember { mutableStateOf<String?>(null) }
    val subjectToApprove = remember { mutableStateOf<String?>(null) }
    val showRemoveConfirmationDialog = remember { mutableStateOf(false) }
    val subjectToConfirmRemove = remember { mutableStateOf<String?>(null) }

    // Fetch subjects based on the user's career
    val materias = when (user?.career) {
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

    // Convert strings to lists and filter out empty subjects
    val allSubjects = user?.allSubjects?.flatMap { it.split(",").map(String::trim).filter { it.isNotEmpty() } } ?: emptyList()
    val currentSubjects = user?.currentSubjects?.flatMap { it.split(",").map(String::trim).filter { it.isNotEmpty() } } ?: emptyList()
    val approvedSubjects = allSubjects

    // Remove current subject effect
    LaunchedEffect(subjectToRemove.value) {
        subjectToRemove.value?.let { subject ->
            userViewModel.updateCurrentSubjects(currentSubjects.toMutableList().apply { remove(subject) }.joinToString(", "))
            subjectToRemove.value = null
            showToast(context, "Materia eliminada: $subject")
        }
    }

    // Remove total subject effect
    LaunchedEffect(totalSubjectToRemove.value) {
        totalSubjectToRemove.value?.let { subject ->
            userViewModel.updateAllSubjects(allSubjects.toMutableList().apply { remove(subject) }.joinToString(", "))
            totalSubjectToRemove.value = null
            showToast(context, "Materia eliminada: $subject")
        }
    }

    // Add current subject effect
    LaunchedEffect(subjectToComplete.value) {
        subjectToComplete.value?.let { subject ->
            if (currentSubjects.size < 6) {
                userViewModel.updateCurrentSubjects(currentSubjects.toMutableList().apply { add(subject) }.joinToString(", "))
                showToast(context, "Materia agregada: $subject")
            } else {
                showToast(context, "No puede llevar más de 6 materias en el ciclo")
            }
            subjectToComplete.value = null
        }
    }

    // Approve current subject effect
    LaunchedEffect(subjectToApprove.value) {
        subjectToApprove.value?.let { subject ->
            val updatedCurrentSubjects = currentSubjects.toMutableList().apply { remove(subject) }
            val updatedAllSubjects = allSubjects.toMutableList().apply { add(subject) }
            userViewModel.updateCurrentSubjects(updatedCurrentSubjects.joinToString(", "))
            userViewModel.updateAllSubjects(updatedAllSubjects.joinToString(", "))
            subjectToApprove.value = null
            showToast(context, "Materia aprobada: $subject")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.vid_fondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "PENSUM", style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF001F3F)))

            Spacer(modifier = Modifier.height(16.dp))

            val totalMaterias = materias.size
            val aprobadasMaterias = approvedSubjects.size
            val avance = if (totalMaterias != 0) (aprobadasMaterias * 100) / totalMaterias else 0

            Text(text = "Progreso en la carrera: ${user?.career}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Materias totales/aprobadas: $totalMaterias/$aprobadasMaterias", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Avance en la carrera: $avance%", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Num materias actuales: ${currentSubjects.size}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = { showCurrentSubjectsDialog.value = true },
                    modifier = Modifier
                        .height(64.dp)
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)
                ) {
                    Text("Materias en curso", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Button(
                    onClick = { showTotalSubjectsDialog.value = true },
                    modifier = Modifier
                        .height(64.dp)
                        .weight(1f)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)
                ) {
                    Text("Materias aprobadas", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showAddSubjectsDialog.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F3F), contentColor = Color.White)
            ) {
                Text("Seleccionar materias en curso", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CICLOS",
                style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF001F3F)),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            ) {
                val materiasAgrupadas = groupBySubjects(materias)
                val numerosRomanos = listOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")

                materiasAgrupadas.toSortedMap().forEach { (ciclo, materias) ->
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_ciclo),
                                        contentDescription = "Icono Ciclo",
                                        tint = Color(0xFF001F3F),
                                        modifier = Modifier.size(24.dp).padding(end = 8.dp)
                                    )
                                    Text(
                                        text = "Ciclo ${numerosRomanos.getOrNull(ciclo - 1) ?: ciclo}",
                                        style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF001F3F)),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
                                materias.sortedBy { it.nombre }.forEach { materia ->
                                    val isCompleted = approvedSubjects.contains(materia.nombre)
                                    val isCurrent = currentSubjects.contains(materia.nombre)
                                    val color = when {
                                        isCompleted -> Color.Blue
                                        isCurrent -> Color(0xFF5c6972)
                                        else -> Color.Black
                                    }
                                    Text(
                                        text = materia.nombre,
                                        color = color,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCurrentSubjectsDialog.value) {
        SubjectListDialog(
            title = "Materias en curso",
            subjects = currentSubjects,
            onDismissRequest = { showCurrentSubjectsDialog.value = false },
            onRemoveSubject = { subject ->
                subjectToConfirmRemove.value = subject
                showRemoveConfirmationDialog.value = true
            },
            onApproveSubject = { subject ->
                subjectToApprove.value = subject
            }
        )
    }

    if (showTotalSubjectsDialog.value) {
        SubjectListDialog(
            title = "Materias aprobadas",
            subjects = allSubjects,
            onDismissRequest = { showTotalSubjectsDialog.value = false },
            onRemoveSubject = { subject ->
                subjectToConfirmRemove.value = subject
                showRemoveConfirmationDialog.value = true
            }
        )
    }

    if (showAddSubjectsDialog.value) {
        AddSubjectsDialog(
            allSubjects = materias.filterNot { it.nombre in currentSubjects || it.nombre in allSubjects },
            onDismissRequest = { showAddSubjectsDialog.value = false },
            onAddCurrentSubject = { subject ->
                if (currentSubjects.size < 6) {
                    userViewModel.updateCurrentSubjects(currentSubjects.toMutableList().apply { add(subject) }.joinToString(", "))
                    showToast(context, "Materia agregada a materias actuales: $subject")
                } else {
                    showToast(context, "No puede llevar más de 6 materias en el ciclo")
                }
            }
        )
    }

    if (showRemoveConfirmationDialog.value) {
        ConfirmRemoveDialog(
            subject = subjectToConfirmRemove.value,
            onConfirm = {
                if (currentSubjects.contains(it)) {
                    subjectToRemove.value = it
                } else if (allSubjects.contains(it)) {
                    totalSubjectToRemove.value = it
                }
                showRemoveConfirmationDialog.value = false
            },
            onDismiss = {
                showRemoveConfirmationDialog.value = false
            }
        )
    }
}

@Composable
fun AddSubjectsDialog(
    allSubjects: List<Materia>,
    onDismissRequest: () -> Unit,
    onAddCurrentSubject: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Agregar materia a cursar", style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF001F3F), fontWeight = FontWeight.Bold)) },
        text = {
            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar materia", style = MaterialTheme.typography.bodySmall) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                val filteredSubjects = allSubjects.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
                if (filteredSubjects.isEmpty()) {
                    Text("No hay materias disponibles", style = MaterialTheme.typography.bodySmall)
                } else {
                    LazyColumn {
                        items(filteredSubjects) { materia ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(materia.nombre, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                                IconButton(onClick = { onAddCurrentSubject(materia.nombre) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Agregar a Materias Actuales")
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismissRequest, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) {
                Text("Cerrar", style = MaterialTheme.typography.bodySmall)
            }
        }
    )
}

@Composable
fun SubjectListDialog(
    title: String,
    subjects: List<String>,
    onDismissRequest: () -> Unit,
    onRemoveSubject: ((String) -> Unit)? = null,
    onApproveSubject: ((String) -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title, style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF001F3F), fontWeight = FontWeight.Bold)) },
        text = {
            if (subjects.isEmpty()) {
                Text("No hay materias agregadas", style = MaterialTheme.typography.bodySmall)
            } else {
                LazyColumn {
                    items(subjects) { subject ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(subject, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                            if (onApproveSubject != null) {
                                IconButton(onClick = { onApproveSubject(subject) }) {
                                    Icon(Icons.Default.Check, contentDescription = "Aprobar materia")
                                }
                            }
                            if (onRemoveSubject != null) {
                                IconButton(onClick = { onRemoveSubject(subject) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar materia")
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismissRequest, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) {
                Text("Cerrar", style = MaterialTheme.typography.bodySmall)
            }
        }
    )
}

@Composable
fun ConfirmRemoveDialog(
    subject: String?,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar eliminación") },
        text = { Text("¿Está seguro de que desea eliminar la materia \"$subject\"?") },
        confirmButton = {
            Button(onClick = { subject?.let { onConfirm(it) } }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun groupBySubjects(materias: List<Materia>): Map<Int, List<Materia>> {
    return materias.groupBy { it.ciclo }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

