package com.castroll.noctua.data.remote.repository

import com.castroll.noctua.data.remote.model.News
import com.castroll.noctua.data.remote.network.RetrofitInstance

class NewsRepository {
    suspend fun fetchNews(): List<News> {
        val response = RetrofitInstance.newsAPI.getNews()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }
}