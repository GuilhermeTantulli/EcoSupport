package com.ecosupport.data.network

import retrofit2.Call
import retrofit2.http.GET
import com.seuapp.data.model.User

interface ApiService {
    @GET("users")
    fun getUsers(): Call<List<User>>
}
