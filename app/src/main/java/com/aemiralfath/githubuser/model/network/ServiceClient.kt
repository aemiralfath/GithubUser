package com.aemiralfath.githubuser.model.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceClient {

    private var builder = Retrofit.Builder()

    private fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun buildServiceClient(): ServiceRepository {
        return builder.baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getInterceptor())
            .build()
            .create(ServiceRepository::class.java)
    }
}