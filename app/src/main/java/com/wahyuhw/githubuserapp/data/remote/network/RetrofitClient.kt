package com.wahyuhw.githubuserapp.data.remote.network

import com.wahyuhw.githubuserapp.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val baseUrl = "https://api.github.com/"

    private val retrofitClient: Retrofit.Builder by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Authorization", BuildConfig.GITHUB_USER_TOKEN)
                val request = requestBuilder.build()
                it.proceed(request)
            }
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }
}