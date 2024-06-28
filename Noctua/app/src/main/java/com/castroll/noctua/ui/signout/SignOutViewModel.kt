package com.castroll.noctua.ui.signout

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castroll.noctua.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SignOutViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "¿Deseas cerrar sesión?"
    }
    val text: LiveData<String> = _text

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun signOut(firebaseAuth: FirebaseAuth, context: Context) {
        try {
            firebaseAuth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            _errorMessage.value = "Logout failed: ${e.message}"
        }
    }
}





