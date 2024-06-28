package com.castroll.noctua.ui.home

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.castroll.noctua.R
import kotlinx.coroutines.delay

val imageResources = listOf(
    R.drawable.img_dei1,
    R.drawable.img_dei3,
    R.drawable.img_dei4,
    R.drawable.img_dei5,
    R.drawable.img_dei6
)

@Composable
fun GifBackground(modifier: Modifier = Modifier, gifResId: Int) {
    Image(
        painter = rememberAsyncImagePainter(gifResId),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    val news by homeViewModel.news.observeAsState(emptyList())
    val context = LocalContext.current
    var currentImageIndex by remember { mutableIntStateOf(0) }
    val imageCount = imageResources.size
    val configuration = LocalConfiguration.current
    val fallbackUrl = "https://uca.edu.sv/"
    val gifResId = R.drawable.vid_fondo

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentImageIndex = (currentImageIndex + 1) % imageCount
        }
    }

    Box {
        // GIF de fondo
        GifBackground(
            modifier = Modifier
                .fillMaxSize(),
            gifResId = gifResId
        )

        // Contenido principal
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            item {
                Text(
                    text = "¡BIENVENIDO A NOCTUA!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }

            item {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .background(Color(0xFF001f3f), RoundedCornerShape(8.dp))
                ) {
                    val boxWidth = maxWidth
                    val boxHeight = if (configuration.screenWidthDp < 600) 200.dp else 250.dp

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(boxHeight)
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 16.dp)
                        ) {
                            Text(
                                text = "¿Has escuchado de la ICPC?",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "La ICPC es una competencia mundial donde equipos universitarios resuelven problemas de programación en un tiempo limitado, promoviendo habilidades algorítmicas y de trabajo en equipo.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                color = Color.White,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        val infiniteTransition = rememberInfiniteTransition(label = "")
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 1.1f,
                            targetValue = 1.2f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(2000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ), label = ""
                        )

                        Image(
                            painter = painterResource(id = R.drawable.img_icpc2),
                            contentDescription = "ICPC Info Image",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale
                                )
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }

            item {
                Text(
                    text = "EVENTOS",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )
            }

            items(news) { newsItem ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val url = newsItem.link.takeIf { !it.isNullOrEmpty() } ?: fallbackUrl
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_calendar),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                newsItem.title?.let { title ->
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                newsItem.body?.let { body ->
                                    Text(
                                        text = body,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CONOCE EL DEPARTAMENTO DE ELECTRONICA E INFORMATICA",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp) // Aumentar el tamaño de la box
                        .background(Color(0xFF001f3f), RoundedCornerShape(8.dp)) // Azul marino
                ) {
                    val transition = updateTransition(currentImageIndex, label = "imageTransition")
                    val alpha by transition.animateFloat(
                        transitionSpec = { tween(durationMillis = 500) }, label = "alpha"
                    ) { state ->
                        if (state == currentImageIndex) 1f else 0f
                    }

                    Image(
                        painter = painterResource(id = imageResources[currentImageIndex]),
                        contentDescription = "Imagen del Departamento",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)) // Redondear los bordes de las imágenes
                            .graphicsLayer(alpha = alpha)
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        val arrowTransition = rememberInfiniteTransition(label = "")
                        val arrowScale by arrowTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.2f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ), label = ""
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Previous",
                            tint = Color.White,
                            modifier = Modifier
                                .size(32.dp)
                                .graphicsLayer(
                                    scaleX = arrowScale,
                                    scaleY = arrowScale
                                )
                                .clickable {
                                    currentImageIndex =
                                        if (currentImageIndex > 0) currentImageIndex - 1 else imageCount - 1
                                }
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier
                                .size(32.dp)
                                .graphicsLayer(
                                    scaleX = arrowScale,
                                    scaleY = arrowScale
                                )
                                .clickable {
                                    currentImageIndex = (currentImageIndex + 1) % imageCount
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}

