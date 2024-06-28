package com.castroll.noctua.data.remote.api

import com.castroll.noctua.data.remote.model.LoginRequest
import com.castroll.noctua.data.remote.model.RegisterRequest
import com.castroll.noctua.data.remote.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServices {
    @POST("auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<AuthResponse>

}