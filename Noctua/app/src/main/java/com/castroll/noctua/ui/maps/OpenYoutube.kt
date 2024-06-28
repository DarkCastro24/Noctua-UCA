package com.castroll.noctua.ui.maps

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.castroll.noctua.R

@Composable
fun OpenYouTubeButton(videoId: String) {
    val context = LocalContext.current

    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
            intent.putExtra("force_fullscreen", true)
            context.startActivity(intent)
        },
        modifier = Modifier
            .width(265.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Text(
            text = "Mira el video",
            fontWeight = FontWeight.Normal,
            fontSize = 22.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.icon_play),
            contentDescription = "Play Icon",
            tint = Color.Black,
            modifier = Modifier.size(20.dp)
        )
    }
}
