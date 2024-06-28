package com.castroll.noctua.data.remote.api

import com.castroll.noctua.data.remote.model.Lab
import retrofit2.Response
import retrofit2.http.GET

interface LabServices {
    @GET("lab")
    suspend fun getLabs(): Response<List<Lab>>
}
