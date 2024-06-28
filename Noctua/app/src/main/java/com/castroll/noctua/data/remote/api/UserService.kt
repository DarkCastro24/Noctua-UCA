package com.castroll.noctua.data.remote.api

import com.castroll.noctua.data.remote.model.User
import com.castroll.noctua.data.remote.model.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserServices {
    @GET("user/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @GET("user/")
    suspend fun getUsers(): Response<UsersResponse>

    @PATCH("user/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body updatedUser: Map<String, String>): Response<User>

    @PUT("user/password/{id}")
    suspend fun updatePassword(@Path("id") id: String, @Body passwordFields: Map<String, String>): Response<Void>
}

