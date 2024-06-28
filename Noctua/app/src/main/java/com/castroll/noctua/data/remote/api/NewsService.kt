package com.castroll.noctua.data.remote.api

import com.castroll.noctua.data.remote.model.News
import retrofit2.Response
import retrofit2.http.GET

interface NewsService {
    @GET("news/")
    suspend fun getNews(): Response<List<News>>
}
