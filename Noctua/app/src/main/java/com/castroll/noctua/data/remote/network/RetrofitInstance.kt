package com.castroll.noctua.data.remote.network

import com.castroll.noctua.data.remote.api.AuthServices
import com.castroll.noctua.data.remote.api.LabServices
import com.castroll.noctua.data.remote.api.NewsService
import com.castroll.noctua.data.remote.api.UserServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://noctua-web-service-rn7lfiej0-diego-castros-projects-acd0258d.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // http://10.0.2.2:3500/api/
    // https://noctua-web-service-rn7lfiej0-diego-castros-projects-acd0258d.vercel.app/api/

    val authApi: AuthServices by lazy {
        retrofit.create(AuthServices::class.java)
    }

    val labApi: LabServices by lazy {
        retrofit.create(LabServices::class.java)
    }

    val newsAPI: NewsService by lazy {
        retrofit.create(NewsService::class.java)
    }

    val userApi: UserServices by lazy {
        retrofit.create(UserServices::class.java)
    }
}