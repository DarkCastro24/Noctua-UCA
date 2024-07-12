package com.castroll.noctua.data.local

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castroll.noctua.data.remote.network.RetrofitInstance
import com.castroll.noctua.data.remote.model.User
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONException
import org.json.JSONObject

class UserViewModel : ViewModel() {
    private val userData = MutableLiveData<User>()
    val user: LiveData<User> get() = userData

    fun setUser(user: User) {
        userData.value = user
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun clearUser() {
        userData.value = null
    }

    fun updateUser(
        currentSubjects: String,
        allSubjects: String,
        career: String?,
        phone: String?,
        email: String?,
        biography: String?,
        hobbies: String?,
        visible: Boolean
    ): Boolean {
        return runBlocking {
            val currentUser = user.value ?: return@runBlocking false
            val updatedFields = mutableMapOf<String, String>()
            updatedFields["currentSubjects"] = currentSubjects
            updatedFields["allSubjects"] = allSubjects
            career?.let { updatedFields["career"] = it }
            phone?.let { updatedFields["phone"] = it }
            email?.let { updatedFields["email"] = it }
            biography?.let { updatedFields["biography"] = it }
            hobbies?.let { updatedFields["hobbies"] = it }
            updatedFields["visible"] = visible.toString()

            try {
                val response = RetrofitInstance.userApi.updateUser(currentUser._id, updatedFields)
                if (response.isSuccessful) {
                    response.body()?.let { updatedUser ->
                        setUser(updatedUser)
                        return@runBlocking true
                    } ?: run {
                        return@runBlocking false
                    }
                } else {
                    return@runBlocking false
                }
            } catch (e: Exception) {
                return@runBlocking false
            }
        }
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String): String? {
        val currentUser = user.value ?: return "Usuario no encontrado"
        return try {
            val response = RetrofitInstance.userApi.updatePassword(
                currentUser._id,
                mapOf("currentPassword" to currentPassword, "password" to newPassword)
            )
            if (response.isSuccessful) {
                null
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    errorBody?.let { JSONObject(it).optString("message", "Error desconocido") }
                } catch (e: JSONException) {
                    errorBody ?: "Error desconocido"
                }
                errorMessage
            }
        } catch (e: Exception) {
            e.message ?: "Error desconocido"
        }
    }

    fun updateCurrentSubjects(newSubjects: String) {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            val updatedFields = mutableMapOf<String, String>()
            updatedFields["currentSubjects"] = newSubjects

            try {
                val response = RetrofitInstance.userApi.updateUser(currentUser._id, updatedFields)
                if (response.isSuccessful) {
                    response.body()?.let { updatedUser ->
                        setUser(updatedUser)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun updateAllSubjects(newSubjects: String) {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            val updatedFields = mutableMapOf<String, String>()
            updatedFields["allSubjects"] = newSubjects

            try {
                val response = RetrofitInstance.userApi.updateUser(currentUser._id, updatedFields)
                if (response.isSuccessful) {
                    response.body()?.let { updatedUser ->
                        setUser(updatedUser)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }

    fun updateAllSubjectsGrades(newGrades: List<Double>) {
        viewModelScope.launch {
            val currentUser = user.value ?: return@launch
            val updatedFields = newGrades.mapIndexed { index, grade ->
                "allSubjectsGrades[$index]" to grade.toString()
            }.toMap()

            try {
                val response = RetrofitInstance.userApi.updateUser(currentUser._id, updatedFields)
                if (response.isSuccessful) {
                    response.body()?.let { updatedUser ->
                        setUser(updatedUser)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
}




