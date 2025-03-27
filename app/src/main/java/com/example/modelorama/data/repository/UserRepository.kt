package com.example.modelorama.data.repository

import com.example.modelorama.data.model.User
import com.example.modelorama.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    // Add proper error handling to your network requests
    private fun registerUser(user: User): Flow<Result<User>> = flow {
        try {
            val response = apiService.registerUser(user)
            if (response.isSuccessful) {
                emit(Result.success(response.body()!!))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(Exception("Network error: ${e.message}", e)))
        }
    }
    
    // Add other repository methods here
}