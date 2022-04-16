package com.wahyuhw.githubuserapp.data.remote.network

import com.wahyuhw.githubuserapp.data.remote.response.SearchResponse
import com.wahyuhw.githubuserapp.data.shared.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<SearchResponse>

    @GET("users/{user}")
    fun getUser(@Path("user") user: String?): Call<User>

    @GET("users/{user}/followers")
    fun getListFollowers(@Path("user") user: String?): Call<List<User>>

    @GET("users/{user}/following")
    fun getListFollowing(@Path("user") user: String?): Call<List<User>>
}