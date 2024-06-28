package com.castroll.noctua.data.remote.repository

import com.castroll.noctua.data.remote.model.Lab
import com.castroll.noctua.data.remote.network.RetrofitInstance

class LabRepository {
    suspend fun fetchLabs(): List<Lab> {
        val response = RetrofitInstance.labApi.getLabs()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }
}