package com.castroll.noctua.ui.laboratories

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.castroll.noctua.R
import com.castroll.noctua.data.remote.model.Lab
import com.castroll.noctua.data.remote.model.Schedule
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LaboratoriesScreen(laboratoriesViewModel: LaboratoriesViewModel = viewModel()) {
    val laboratories by laboratoriesViewModel.laboratories.observeAsState(emptyList())
    val outfitRegular = FontFamily(Font(R.font.outfitregular))

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondoooooo222),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "LABORATORIOS",
                style = MaterialTheme.typography.headlineMedium.copy(fontFamily = outfitRegular),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(laboratories) { lab ->
                    LaboratoryCard(lab, outfitRegular)
                }
            }
        }
    }
}

@Composable
fun LaboratoryCard(lab: Lab, outfitRegular: FontFamily) {
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    if (showDialog) {
        ScheduleDialog(
            lab = lab,
            onDismiss = { setShowDialog(false) },
            outfitRegular = outfitRegular
        )
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(lab.urlImage),
                contentDescription = "Laboratory Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(128.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            ) {
                Text(
                    text = lab.labnumber,
                    style = MaterialTheme.typography.titleSmall.copy(fontFamily = outfitRegular),
                    fontSize = 16.sp
                )
                Text(
                    text = "Descripción:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontFamily = outfitRegular),
                    fontSize = 12.sp
                )
                Text(
                    text = lab.description,
                    color = Color(0xFF5F6F92),
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular),
                    fontSize = 12.sp
                )
                Text(
                    text = "Capacidad:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontFamily = outfitRegular),
                    fontSize = 12.sp
                )
                Text(
                    text = "${lab.alumAmount} estudiantes",
                    color = Color(0xFF5F6F92),
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { setShowDialog(true) },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                        .defaultMinSize(minWidth = 1.dp)
                ) {
                    Text(
                        text = "Mostrar Horario",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun ScheduleDialog(lab: Lab, onDismiss: () -> Unit, outfitRegular: FontFamily) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEEE", Locale("es", "ES"))
    val dayNumberFormat = SimpleDateFormat("d", Locale.getDefault())

    val today = Date()
    val days = List(5) { index -> Date(System.currentTimeMillis() + index * 24 * 60 * 60 * 1000) }
    val dateStrings = days.map { dateFormat.format(it) }
    val dayStrings = days.map { formatDayString(dayFormat.format(it)) }
    val dayNumbers = days.map { dayNumberFormat.format(it) }

    val currentPage = remember { mutableStateOf(0) }
    val selectedDate = remember { mutableStateOf(dateStrings[currentPage.value]) }

    val predefinedHours = listOf(
        "09:00", "11:00", "13:00", "15:00", "17:00"
    )

    fun updateScheduleForSelectedDate() {
        selectedDate.value = dateStrings[currentPage.value]
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Actividades de: ${lab.labnumber}",
                style = MaterialTheme.typography.headlineSmall.copy(fontFamily = outfitRegular),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column {
                val selectedDayText = if (selectedDate.value == dateFormat.format(today)) {
                    "${dayFormat.format(dateFormat.parse(selectedDate.value)!!)} (hoy)"
                } else {
                    dayFormat.format(dateFormat.parse(selectedDate.value)!!)
                }
                Text(
                    text = "Día seleccionado: $selectedDayText",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = outfitRegular)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (currentPage.value > 0) {
                                currentPage.value -= 1
                                updateScheduleForSelectedDate()
                            }
                        },
                        enabled = currentPage.value > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF365b77))
                    ) {
                        Text(text = "<")
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${dayStrings[currentPage.value]} ${dayNumbers[currentPage.value]}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = outfitRegular)
                        )
                    }
                    Button(
                        onClick = {
                            if (currentPage.value < dateStrings.size - 1) {
                                currentPage.value += 1
                                updateScheduleForSelectedDate()
                            }
                        },
                        enabled = currentPage.value < dateStrings.size - 1,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF365b77))
                    ) {
                        Text(text = ">")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Fecha: ${selectedDate.value}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = outfitRegular)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(predefinedHours) { hour ->
                        val schedulesForSelectedDate = lab.schedule.filter { it.date == selectedDate.value }
                        val schedule = schedulesForSelectedDate.find { it.hour == hour }
                        ScheduleItem(schedule, hour, outfitRegular)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK", fontFamily = outfitRegular)
            }
        }
    )
}

@Composable
fun ScheduleItem(schedule: Schedule?, hour: String, outfitRegular: FontFamily) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Horario: $hour",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = outfitRegular)
        )
        Spacer(modifier = Modifier.height(4.dp))
        if (schedule != null) {
            Text(
                text = "Actividad: ${schedule.activity ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = outfitRegular)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Disponible: ",
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = outfitRegular)
                )
                if (schedule.available) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF477c36)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
        } else {
            Text(
                text = "Horario libre, sin actividades",
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = outfitRegular)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Disponible: ",
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = outfitRegular)
                )
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF477c36)
                )
            }
        }
    }
}

fun formatDayString(day: String): String {
    return day.replaceFirstChar { it.uppercaseChar() }
}
