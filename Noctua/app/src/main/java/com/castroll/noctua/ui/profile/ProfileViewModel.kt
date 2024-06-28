package com.castroll.noctua.ui.profile

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castroll.noctua.data.remote.network.RetrofitInstance
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Profile Screen"
    }
    val text: LiveData<String> = _text

    fun updateUserProfile(
        userId: String,
        email: String,
        phone: String,
        biography: String,
        hobbies: String,
        career: String,
        visible: String,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userApi.updateUser(
                    userId, mapOf(
                        "career" to career,
                        "phone" to phone,
                        "email" to email,
                        "biography" to biography,
                        "hobbies" to hobbies,
                        "visible" to visible
                    )
                )
                if (response.isSuccessful) {
                    Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error al actualizar el perfil: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error al actualizar el perfil: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
