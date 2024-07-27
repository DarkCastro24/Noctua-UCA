package com.castroll.noctua.ui.pensum

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
    val outfitRegular = FontFamily(Font(R.font.outfitregular))

    val showCurrentSubjectsDialog = remember { mutableStateOf(false) }
    val showTotalSubjectsDialog = remember { mutableStateOf(false) }
    val showAddSubjectsDialog = remember { mutableStateOf(false) }
    val subjectToRemove = remember { mutableStateOf<String?>(null) }
    val subjectToComplete = remember { mutableStateOf<String?>(null) }
    val totalSubjectToRemove = remember { mutableStateOf<String?>(null) }
    val subjectToApprove = remember { mutableStateOf<Pair<String, Double>?>(null) }
    val showRemoveConfirmationDialog = remember { mutableStateOf(false) }
    val subjectToConfirmRemove = remember { mutableStateOf<String?>(null) }
    val isCurrentSubject = remember { mutableStateOf(true) }

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

    // Ensure allSubjectsGrades is a List<Double>
    val allSubjectsGrades: List<Double> = approvedSubjects.mapNotNull {
        it.split(" - ").getOrNull(1)?.toDoubleOrNull()
    }

    // Calculate Total Units and Total Merit Units
    val totalUnits = approvedSubjects.mapNotNull {
        val parts = it.split(" - ")
        val subjectName = parts[0]
        val uv = materias.find { m -> m.nombre == subjectName }?.uv
        uv
    }.sum()

    val totalMeritUnits = approvedSubjects.mapNotNull {
        val parts = it.split(" - ")
        val subjectName = parts[0]
        val grade = parts.getOrNull(1)?.toDoubleOrNull()
        val uv = materias.find { m -> m.nombre == subjectName }?.uv
        if (grade != null && uv != null) grade * uv else null
    }.sum()

    val cum = if (totalUnits != 0) {
        totalMeritUnits / totalUnits
    } else {
        0.0
    }

    // Remove current subject effect
    LaunchedEffect(subjectToRemove.value) {
        subjectToRemove.value?.let { subject ->
            if (isCurrentSubject.value) {
                userViewModel.updateCurrentSubjects(currentSubjects.toMutableList().apply { remove(subject) }.joinToString(", "))
            } else {
                userViewModel.updateAllSubjects(allSubjects.toMutableList().apply { remove(subject) }.joinToString(", "))
            }
            subjectToRemove.value = null
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
        subjectToApprove.value?.let { (subject, grade) ->
            val updatedCurrentSubjects = currentSubjects.toMutableList().apply { remove(subject) }
            val updatedAllSubjects = allSubjects.toMutableList().apply { add("$subject - $grade") }
            userViewModel.updateCurrentSubjects(updatedCurrentSubjects.joinToString(", "))
            userViewModel.updateAllSubjects(updatedAllSubjects.joinToString(", "))
            subjectToApprove.value = null
            showToast(context, "Materia aprobada: $subject con nota $grade")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondoooooo222),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "PROGRESO EN: ${user?.career}",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color(0xFF001F3F), fontFamily = outfitRegular)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val totalMaterias = materias.size
            val aprobadasMaterias = approvedSubjects.size
            val avance = if (totalMaterias != 0) (aprobadasMaterias * 100) / totalMaterias else 0

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Materias totales/aprobadas: $totalMaterias/$aprobadasMaterias", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Avance en la carrera: $avance%", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Num materias actuales: ${currentSubjects.size}", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "CUM de la carrera: ${String.format("%.2f", cum)}", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
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
                    Text("Materias en curso", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontFamily = outfitRegular), maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                    Text("Materias aprobadas", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontFamily = outfitRegular), maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                Text("Seleccionar materias en curso", style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontFamily = outfitRegular), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CICLOS",
                style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF001F3F), fontFamily = outfitRegular),
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
                                        style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF001F3F), fontFamily = outfitRegular),
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
                                materias.sortedBy { it.nombre }.forEach { materia ->
                                    val isCompleted = approvedSubjects.any { it.split(" - ")[0] == materia.nombre }
                                    val isCurrent = currentSubjects.any { it.split(" - ")[0] == materia.nombre }
                                    val color = when {
                                        isCompleted -> Color.Blue
                                        isCurrent -> Color(0xFF5c6972)
                                        else -> Color.Black
                                    }
                                    Text(
                                        text = "${materia.nombre} - ${materia.uv} UV",
                                        color = color,
                                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular),
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
        CurrentSubjectsDialog(
            currentSubjects = currentSubjects,
            onDismissRequest = { showCurrentSubjectsDialog.value = false },
            onRemoveSubject = { subject ->
                subjectToConfirmRemove.value = subject
                isCurrentSubject.value = true
                showRemoveConfirmationDialog.value = true
            },
            onApproveSubject = { subject, grade ->
                userViewModel.updateCurrentSubjects(currentSubjects.toMutableList().apply { remove(subject) }.joinToString(", "))
                subjectToApprove.value = subject to grade
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
                isCurrentSubject.value = false
                showRemoveConfirmationDialog.value = true
            }
        )
    }

    if (showAddSubjectsDialog.value) {
        AddSubjectsDialog(
            allSubjects = materias,
            currentSubjects = currentSubjects,
            approvedSubjects = approvedSubjects,
            onDismissRequest = { showAddSubjectsDialog.value = false },
            onAddCurrentSubject = { subject ->
                if (currentSubjects.size < 6) {
                    userViewModel.updateCurrentSubjects(currentSubjects.toMutableList().apply { add(subject) }.joinToString(", "))
                    showToast(context, "Materia agregada: $subject")
                } else {
                    showToast(context, "El número máximo de materias por ciclo es 6")
                }
            }
        )
    }

    if (showRemoveConfirmationDialog.value) {
        ConfirmRemoveDialog(
            subject = subjectToConfirmRemove.value,
            onConfirm = {
                if (isCurrentSubject.value) {
                    subjectToRemove.value = it
                } else {
                    val (subjectName, grade) = it.split(" - ")
                    userViewModel.updateAllSubjects(allSubjects.toMutableList().apply { remove(it) }.joinToString(", "))
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
fun CurrentSubjectsDialog(
    currentSubjects: List<String>,
    onDismissRequest: () -> Unit,
    onRemoveSubject: (String) -> Unit,
    onApproveSubject: (String, Double) -> Unit
) {
    val outfitRegular = FontFamily(Font(R.font.outfitregular))
    val subjectGrades = remember { mutableStateMapOf<String, String>() }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Materias en curso", style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF001F3F), fontWeight = FontWeight.Bold, fontFamily = outfitRegular)) },
        text = {
            if (currentSubjects.isEmpty()) {
                Text("No hay materias en curso", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
            } else {
                LazyColumn {
                    items(currentSubjects) { subject ->
                        var grade by remember { mutableStateOf(subjectGrades[subject] ?: "") }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(subject, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
                            TextField(
                                value = grade,
                                onValueChange = {
                                    grade = it
                                    subjectGrades[subject] = it
                                },
                                label = { Text("Nota", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular)) },
                                modifier = Modifier.width(80.dp)
                            )
                            IconButton(onClick = {
                                val gradeValue = grade.toDoubleOrNull() ?: 0.0
                                onApproveSubject(subject, gradeValue)
                                subjectGrades[subject] = "" // Clear the grade
                            }) {
                                Icon(Icons.Default.Check, contentDescription = "Aprobar materia")
                            }
                            IconButton(onClick = { onRemoveSubject(subject) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar materia")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismissRequest, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) {
                Text("Cerrar", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
            }
        }
    )
}

@Composable
fun AddSubjectsDialog(
    allSubjects: List<Materia>,
    currentSubjects: List<String>,
    approvedSubjects: List<String>,
    onDismissRequest: () -> Unit,
    onAddCurrentSubject: (String) -> Unit
) {
    val outfitRegular = FontFamily(Font(R.font.outfitregular))
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    val availableSubjects = allSubjects.filterNot { materia ->
        val materiaName = materia.nombre
        currentSubjects.any { it.split(" - ")[0] == materiaName } ||
                approvedSubjects.any {
                    val parts = it.split(" - ")
                    parts[0] == materiaName && (parts.getOrNull(1)?.toDoubleOrNull() ?: 0.0) > 6.0
                }
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Seleccionar materias en curso", style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF001F3F), fontWeight = FontWeight.Bold, fontFamily = outfitRegular)) },
        text = {
            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar materia", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                val filteredSubjects = availableSubjects.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
                if (filteredSubjects.isEmpty()) {
                    Text("No hay materias disponibles", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
                } else {
                    LazyColumn {
                        items(filteredSubjects) { materia ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        if (currentSubjects.size < 6) {
                                            onAddCurrentSubject(materia.nombre)
                                            showToast(context, "Materia agregada: ${materia.nombre}")
                                            onDismissRequest()
                                        } else {
                                            showToast(context, "El número máximo de materias por ciclo es 6")
                                        }
                                    }
                            ) {
                                Text(materia.nombre, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismissRequest, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(16.dp)) {
                Text("Cerrar", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
            }
        }
    )
}

@Composable
fun SubjectListDialog(
    title: String,
    subjects: List<String>,
    onDismissRequest: () -> Unit,
    onRemoveSubject: ((String) -> Unit)? = null
) {
    val outfitRegular = FontFamily(Font(R.font.outfitregular))
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title, style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF001F3F), fontWeight = FontWeight.Bold, fontFamily = outfitRegular)) },
        text = {
            if (subjects.isEmpty()) {
                Text("No hay materias agregadas", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
            } else {
                LazyColumn {
                    items(subjects) { subject ->
                        val parts = subject.split(" - ")
                        val subjectName = parts[0]
                        val grade = parts.getOrNull(1)?.toDoubleOrNull()
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text(subjectName, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
                            if (grade != null && grade != 0.0) {
                                Text(grade.toString(), style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
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
                Text("Cerrar", style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular))
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

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun groupBySubjects(materias: List<Materia>): Map<Int, List<Materia>> {
    return materias.groupBy { it.ciclo }
}