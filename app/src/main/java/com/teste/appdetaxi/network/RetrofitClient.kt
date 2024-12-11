package com.teste.appdetaxi.network

import com.teste.appdetaxi.api.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




object RetrofitClient {

    private const val BASE_URL = "https://xd5zl5kk2yltomvw5fb37y3bm40vsyrx.lambda-url.sa-east-1.on.aws"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}