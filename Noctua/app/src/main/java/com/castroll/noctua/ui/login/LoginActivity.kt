package com.castroll.noctua.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import android.app.AlertDialog
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.castroll.noctua.MainActivity
import com.castroll.noctua.R
import com.castroll.noctua.data.remote.model.LoginRequest
import com.castroll.noctua.data.remote.model.RegisterRequest
import com.castroll.noctua.data.remote.network.RetrofitInstance
import com.castroll.noctua.data.local.UserViewModel
import com.castroll.noctua.data.remote.model.User
import com.castroll.noctua.di.MyApp
import com.castroll.noctua.ui.firstuse.FirstUseActivity
import com.castroll.noctua.utils.generatePasswordFromUsername
import com.castroll.noctua.utils.sendEmail
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : ComponentActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    private val userViewModel: UserViewModel by lazy {
        (application as MyApp).viewModelProvider[UserViewModel::class.java]
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("156350353897-vl687deajh3a2f64k5f50oiaqusc8bnk.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Prepare the ActivityResultLauncher
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            }
        }
        setContent {
            LoginScreen()
        }
    }

    // GOOGLE LOGIN WITH FIREBASE
    @Composable
    fun LoginScreen() {
        val spacing = 16.dp
        val navyBlue = Color(0xFF001F3F)

        val backgroundImages = listOf(
            painterResource(id = R.drawable.img_fondo_login),
            painterResource(id = R.drawable.img_fondo_login2),
            painterResource(id = R.drawable.img_fondo_login3),
            painterResource(id = R.drawable.img_fondo_login4)
        )

        var currentImageIndex by remember { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(5000)
                currentImageIndex = (currentImageIndex + 1) % backgroundImages.size
            }
        }

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var loginMessage by remember { mutableStateOf<String?>(null) }
        var showErrorDialog by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

        // Cargar la fuente personalizada
        val outfitRegular = FontFamily(Font(R.font.outfitregular))

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = backgroundImages[currentImageIndex],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = spacing)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.imagen2222),
                        contentDescription = null,
                        tint = navyBlue,
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        text = "Noctua UCA",
                        color = navyBlue,
                        style = MaterialTheme.typography.titleSmall.copy(fontFamily = outfitRegular),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "El progreso de la tecnología nos da la oportunidad de mejorar el mundo.",
                        style = MaterialTheme.typography.titleLarge.copy(fontFamily = outfitRegular),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "- Bill Gates",
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = outfitRegular),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextFieldWithIcon(
                        iconResId = R.drawable.icon_usuario,
                        placeholder = "Usuario",
                        keyboardType = KeyboardType.Text,
                        modifier = Modifier.width(280.dp),
                        text = username,
                        onValueChange = { username = it.trim() },
                        iconSize = 20.dp,
                        outfitRegular = outfitRegular
                    )
                    Spacer(modifier = Modifier.height(spacing))
                    TextFieldWithPasswordIcon(
                        iconResId = R.drawable.icon_contrasena,
                        placeholder = "Contraseña",
                        keyboardType = KeyboardType.Password,
                        modifier = Modifier.width(280.dp),
                        text = password,
                        onValueChange = { password = it.trim() },
                        iconSize = 20.dp,
                        trailingIconSize = 16.dp,
                        outfitRegular = outfitRegular
                    )
                    Spacer(modifier = Modifier.height(spacing))
                    Button(
                        onClick = {
                            if (username.isBlank() || password.isBlank()) {
                                errorMessage = "Por favor, complete todos los campos."
                                showErrorDialog = true
                            } else {
                                coroutineScope.launch {
                                    loginCredentials(username, password) { message, isError ->
                                        if (isError) {
                                            errorMessage = message
                                            showErrorDialog = true
                                        } else {
                                            loginMessage = message
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = navyBlue),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Iniciar Sesión", color = Color.White, fontSize = 16.sp, fontFamily = outfitRegular)
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { signInGoogleAlert() },
                        colors = ButtonDefaults.buttonColors(containerColor = navyBlue),
                        modifier = Modifier
                            .width(240.dp)
                            .height(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_google),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Iniciar con Google", color = Color.White, fontSize = 16.sp, fontFamily = outfitRegular)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showErrorDialog = false
                    },
                    title = { Text("Error al iniciar sesión", fontFamily = outfitRegular) },
                    text = { Text(errorMessage, fontFamily = outfitRegular) },
                    confirmButton = {
                        Button(
                            onClick = { showErrorDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = navyBlue)
                        ) {
                            Text("OK", fontFamily = outfitRegular)
                        }
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextFieldWithIcon(
        iconResId: Int,
        placeholder: String,
        keyboardType: KeyboardType,
        modifier: Modifier = Modifier,
        text: String,
        onValueChange: (String) -> Unit,
        iconSize: Dp = 24.dp, // Add a parameter for icon size with a default value
        outfitRegular: FontFamily // Add a parameter for the font family
    ) {
        val navyBlue = Color(0xFF001F3F) // Color azul marino

        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontFamily = outfitRegular) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (keyboardType == KeyboardType.Password) PasswordVisualTransformation() else VisualTransformation.None,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize) // Set the size of the icon
                )
            },
            modifier = modifier,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                focusedBorderColor = navyBlue, // Azul marino
                unfocusedBorderColor = navyBlue, // Azul marino
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TextFieldWithPasswordIcon(
        iconResId: Int,
        placeholder: String,
        keyboardType: KeyboardType,
        modifier: Modifier = Modifier,
        text: String,
        onValueChange: (String) -> Unit,
        iconSize: Dp = 24.dp, // Add a parameter for icon size with a default value
        trailingIconSize: Dp = 16.dp, // Add a parameter for the trailing icon size
        outfitRegular: FontFamily // Add a parameter for the font family
    ) {
        val navyBlue = Color(0xFF001F3F) // Color azul marino
        var passwordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontFamily = outfitRegular) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize) // Set the size of the icon
                )
            },
            trailingIcon = {
                if (keyboardType == KeyboardType.Password) {
                    val imageResId = if (passwordVisible) R.drawable.icon_ver else R.drawable.icon_no_ver

                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(painter = painterResource(id = imageResId), contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña", modifier = Modifier.size(trailingIconSize))
                    }
                }
            },
            modifier = modifier,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                focusedBorderColor = navyBlue, // Azul marino
                unfocusedBorderColor = navyBlue, // Azul marino
            )
        )
    }

    private fun signInGoogleAlert() {
        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            } else {
                Toast.makeText(this, "Error al cerrar sesión: ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        if (completedTask.isSuccessful) {
            try {
                val account = completedTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
                account.email?.let { email ->
                    if (email.endsWith("@uca.edu.sv") || email == "darkcastroll@gmail.com") {
                        val name = account.displayName ?: ""
                        val profilePhoto = account.photoUrl.toString()
                        val type = if (email == "darkcastroll@gmail.com" || !email.substringBefore("@").take(4).all { it.isDigit() }) 0 else 1
                        coroutineScope.launch {
                            val (user, message, password) = withContext(Dispatchers.IO) {
                                loginGoogle(email, name, profilePhoto, type)
                            }
                            if (user != null) {
                                if (user.biography.isNullOrEmpty()) {
                                    showFirstTimeAlert(email, password)
                                } else {
                                    goToMainActivity()
                                }
                            } else {
                                showAlert("Inicio de sesión fallido", "No se pudo conectar con el servidor, revise su conexión a internet.")
                                signOut()
                            }
                        }
                    } else {
                        showAlertEmailInvalid()
                        signOut()
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Error en el inicio de sesión: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                signOut()
            }
        } else {
            Toast.makeText(this, "Fallo en el inicio de sesión.", Toast.LENGTH_SHORT).show()
            signOut()
        }
    }

    private fun showFirstTimeAlert(email: String, password: String) {
        val termsAndConditions = """
        La aplicación Noctua UCA está destinada exclusivamente para uso educativo dentro de la universidad. Al utilizar esta aplicación, usted acepta las siguientes condiciones:

        1. La aplicación obtendrá datos de su cuenta de Google, incluyendo su foto de perfil, nombre completo y dirección de correo electrónico.
        
        2. Los datos recopilados se utilizarán únicamente con fines educativos y para mejorar la experiencia del usuario dentro de la universidad.
        
        3. Usted se compromete a utilizar la aplicación de manera responsable y a no compartir sus credenciales con nadie.

        En caso de aceptar los términos y condiciones, sus credenciales para acceder a Noctua UCA serán las siguientes, es opcional si desea actualizarlas en la seccion edición de perfil:
        
        Contara con la opción de iniciar sesión mediante el uso de credenciales en caso de no contar con la cuenta en algún dispositivo, en su correo electronico recibira las credenciales para su cuenta de Noctua.
    """.trimIndent()

        val spannableString = SpannableString(termsAndConditions)
        val boldText = "en su correo electronico recibira las credenciales para su cuenta de Noctua."
        val startIndex = termsAndConditions.indexOf(boldText)
        if (startIndex != -1) {
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                startIndex + boldText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        AlertDialog.Builder(this)
            .setTitle("Términos y condiciones de Noctua UCA")
            .setMessage(spannableString)
            .setPositiveButton("Aceptar términos") { dialog, _ ->
                coroutineScope.launch {
                    withContext(Dispatchers.IO) {
                        sendEmail(email, password)
                    }
                    dialog.dismiss()
                    goToMainActivity()
                }
            }
            .setCancelable(false)
            .show()
    }

    private suspend fun loginGoogle(email: String, name: String, profilePhoto: String, type: Int): Triple<User?, String, String> {
        try {
            val password = generatePasswordFromUsername(email)

            val maxRetries = 3
            var currentRetry = 0
            var user: User? = null
            var message = "Unknown error"

            while (currentRetry < maxRetries && user == null) {
                val response = RetrofitInstance.authApi.registerUser(RegisterRequest(email, name, password, profilePhoto, type))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.user != null) {
                        user = body.user
                        withContext(Dispatchers.Main) {
                            userViewModel.setUser(user)
                        }
                        message = "Registration Successful"
                    } else {
                        message = body?.error ?: "Unknown error"
                    }
                } else {
                    message = response.errorBody()?.string()?.let { parseError(it) } ?: "Unknown server error"
                }

                if (user == null) {
                    currentRetry++
                    if (currentRetry < maxRetries) {
                        delay(1000)
                    }
                }
            }
            return Triple(user, message, password)
        } catch (e: Exception) {
            return Triple(null, "Registration Failed: ${e.message ?: "Unknown error"}", "")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val email = auth.currentUser?.email
                if (email != null && email.endsWith("@uca.edu.sv")) {
                    //goToMainActivity()
                } else {
                    signOut()
                }
            } else {
                Toast.makeText(this, "Fallo en la autenticación con Firebase.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAlertEmailInvalid() {
        AlertDialog.Builder(this)
            .setTitle("Correo no válido")
            .setMessage("Por favor, inicie sesión con un correo @uca.edu.sv")
            .setPositiveButton("Aceptar", null)
            .show()
            .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color(0xFF001F3F))
    }

    private suspend fun loginCredentials(username: String, password: String, onResult: (String, Boolean) -> Unit) {
        try {
            val trimmedUsername = username.trim()
            val trimmedPassword = password.trim()

            if (trimmedUsername.isEmpty() || trimmedPassword.isEmpty()) {
                onResult("Por favor, complete todos los campos.", true)
                return
            }

            val response = RetrofitInstance.authApi.loginUser(LoginRequest(trimmedUsername, trimmedPassword))
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.user != null) {
                        val user = response.body()?.user!!
                        userViewModel.setUser(user)
                        goToMainActivity()
                    } else {
                        onResult("Login Failed: ${it.error ?: "Unknown error"}", true)
                    }
                }
            } else {
                val errorResponse = response.errorBody()?.string()?.let { parseError(it) } ?: "Unknown server error"
                onResult(errorResponse, true)
            }
        } catch (e: Exception) {
            onResult("Login Failed: ${e.message ?: "Unknown error"}", true)
        }
    }

    private fun goToMainActivity() {
        val intent: Intent
        val username = userViewModel.user.value?.username

        intent = if (username == null) {
            Intent(this, FirstUseActivity::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun signOut() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
        }
    }

    fun parseError(jsonResponse: String): String {
        return try {
            JSONObject(jsonResponse).getString("error")
        } catch (e: JSONException) {
            "Error parsing error message"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun Any.setTextColor(color: Color) {
        // Extension function to set text color
    }
}
