package com.example.modelorama.data.network

import com.example.modelorama.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users/register")
    suspend fun registerUser(@Body user: User): Response<User>
    
    // Add other API endpoints as needed
}