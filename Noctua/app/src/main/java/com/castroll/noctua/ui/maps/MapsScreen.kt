package com.castroll.noctua.ui.maps

import ChoiceDialog
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.castroll.noctua.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapsScreen(mapsViewModel: MapsViewModel = viewModel()) {
    var scale by remember { mutableFloatStateOf(1f) }
    var zoomedIn by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var showDialog by remember { mutableStateOf(false) }
    val showDirections = remember { mutableStateOf(false) }
    var currentMapResource by remember { mutableStateOf(R.drawable.img_new_map2) }
    var videoId by remember { mutableStateOf("") }
    var imageWidth by remember { mutableStateOf(0f) }
    var imageHeight by remember { mutableStateOf(0f) }
    val mapsText by mapsViewModel.text.observeAsState("")

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.vid_fondo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mapa del Campus UCA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            )
                            .pointerInteropFilter { event ->
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        if (zoomedIn) {
                                            scale = 1f
                                            offset = Offset.Zero
                                            zoomedIn = false
                                        } else {
                                            val centerX = imageWidth / 2f
                                            val centerY = imageHeight / 2f
                                            val tapX = event.x - centerX / scale
                                            val tapY = event.y - centerY / scale
                                            offset = Offset(-tapX, -tapY)
                                            scale = 1.6f
                                            zoomedIn = true
                                        }
                                        true
                                    }
                                    else -> false
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(id = currentMapResource),
                            contentDescription = "Zoomable image",
                            contentScale = ContentScale.FillWidth, // Ensure the image fits within the bounds
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    val size = coordinates.size.toSize()
                                    imageWidth = size.width
                                    imageHeight = size.height
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(if (zoomedIn) 300.dp else 20.dp))
                    if (!showDirections.value) {
                        Text(
                            text = "¿Hacia donde vas?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                showDialog = true
                            },
                            modifier = Modifier.width(265.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF001F3F))
                        ) {
                            Text(
                                text = "Seleccionar ubicación",
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                    }
                    if (showDirections.value) {
                        Text(
                            text = "Opciones",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OpenYouTubeButton(videoId = videoId)
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                showDirections.value = false
                                currentMapResource = R.drawable.img_new_map2
                            },
                            modifier = Modifier.width(265.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF001F3F))
                        ) {
                            Text(
                                text = "Ya llegue",
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                    }
                    if (showDialog) {
                        ChoiceDialog(
                            showDialog = { showDialog = it },
                            setPosition = { from, to ->
                                when (from) {
                                    "Peatonal" -> {
                                        when (to) {
                                            "Aulas Magnas" -> currentMapResource = R.drawable.map_peatonal_magnas
                                            "Laboratorios de info" -> currentMapResource = R.drawable.map_peatonal_dei
                                            "Laboratorios fisica" -> currentMapResource = R.drawable.map_peatonal_cef
                                            "Biblioteca Florentino" -> currentMapResource = R.drawable.map_peatonal_biblioteca
                                            "Cafeteria central" -> currentMapResource = R.drawable.map_peatonal_cafeteria
                                            "Cubiculos profesores" -> currentMapResource = R.drawable.map_peatonal_modulos
                                        }
                                    }
                                    "La Sultana" -> {
                                        when (to) {
                                            "Aulas Magnas" -> currentMapResource = R.drawable.map_sultana_magnas
                                            "Laboratorios de info" -> currentMapResource = R.drawable.map_sultana_dei
                                            "Laboratorios fisica" -> currentMapResource = R.drawable.map_sultana_cef
                                            "Biblioteca Florentino" -> currentMapResource = R.drawable.map_sultana_biblioteca
                                            "Cafeteria central" -> currentMapResource = R.drawable.map_sultana_cafeteria
                                            "Cubiculos profesores" -> currentMapResource = R.drawable.map_sultana_modulos
                                        }
                                    }
                                    "Los Próceres" -> {
                                        when (to) {
                                            "Aulas Magnas" -> currentMapResource = R.drawable.map_proceres_magnas
                                            "Laboratorios de info" -> currentMapResource = R.drawable.map_proceres_dei
                                            "Laboratorios fisica" -> currentMapResource = R.drawable.map_proceres_cef
                                            "Biblioteca Florentino" -> currentMapResource = R.drawable.map_proceres_biblioteca
                                            "Cafeteria central" -> currentMapResource = R.drawable.map_proceres_cafeteria
                                            "Cubiculos profesores" -> currentMapResource = R.drawable.map_proceres_modulos
                                        }
                                    }
                                }
                                showDirections.value = true
                            },
                            updateVideoId = { video ->
                                videoId = video
                            }
                        )
                    }
                }
            }
        }
    }
}