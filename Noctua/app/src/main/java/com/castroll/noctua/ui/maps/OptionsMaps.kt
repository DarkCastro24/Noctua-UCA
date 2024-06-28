import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ChoiceDialog(showDialog: (Boolean) -> Unit, setPosition: (String, String) -> Unit, updateVideoId: (String) -> Unit) {
    val context = LocalContext.current
    var fromLocation by remember { mutableStateOf("Peatonal") }

    Dialog(
        onDismissRequest = { showDialog(false) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Column(
            modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .width(300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¿De dónde vienes?",
                modifier = Modifier.padding(bottom = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            Button(
                onClick = {
                    fromLocation = "Peatonal"
                    Toast.makeText(context, "Entrada Peatonal UCA selected", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    if (fromLocation == "Peatonal") Color(0xFF001F3F) else Color.LightGray
                )
            ) {
                Text(
                    text = "Entrada Peatonal UCA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    fromLocation = "La Sultana"
                    Toast.makeText(context, "Entrada La Sultana selected", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    if (fromLocation == "La Sultana") Color(0xFF001F3F) else Color.LightGray
                )
            ) {
                Text(
                    text = "Entrada de La Sultana",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    fromLocation = "Los Próceres"
                    Toast.makeText(context, "Entrada Los Proceres selected", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    if (fromLocation == "Los Próceres") Color(0xFF001F3F) else Color.LightGray
                )
            ) {
                Text(
                    text = "Entrada de Los Proceres",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hacia:",
                modifier = Modifier.padding(bottom = 16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Button(
                onClick = {
                    Toast.makeText(context, "Aulas Magnas selected", Toast.LENGTH_SHORT).show()
                    setPosition(fromLocation, "Aulas Magnas")
                    updateVideoId("wEaPlohyzDE")
                    showDialog(false)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF001F3F))
            ) {
                Text(
                    text = "Aulas Magnas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Laboratorios de info selected", Toast.LENGTH_SHORT).show()
                    setPosition(fromLocation, "Laboratorios de info")
                    updateVideoId("Oa9OfMBfnu8")
                    showDialog(false)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF001F3F))
            ) {
                Text(
                    text = "Laboratorios de info",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Laboratorios fisica selected", Toast.LENGTH_SHORT).show()
                    setPosition(fromLocation, "Laboratorios fisica")
                    updateVideoId("Oa9OfMBfnu8")
                    showDialog(false)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF001F3F))
            ) {
                Text(
                    text = "Laboratorios fisica",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Biblioteca Florentino selected", Toast.LENGTH_SHORT).show()
                    setPosition(fromLocation, "Biblioteca Florentino")
                    updateVideoId("-jL9jmQHR0g")
                    showDialog(false)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF001F3F))
            ) {
                Text(
                    text = "Biblioteca Florentino",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Cafeteria central selected", Toast.LENGTH_SHORT).show()
                    setPosition(fromLocation, "Cafeteria central")
                    updateVideoId("xgz_4gT-vrM")
                    showDialog(false)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF001F3F))
            ) {
                Text(
                    text = "Cafeteria central",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Cubiculos profesores selected", Toast.LENGTH_SHORT).show()
                    setPosition(fromLocation, "Cubiculos profesores")
                    updateVideoId("Z5V_3-afZzw")
                    showDialog(false)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF001F3F))
            ) {
                Text(
                    text = "Cubiculos profesores",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}

