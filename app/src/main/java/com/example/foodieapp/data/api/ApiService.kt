package com.example.foodieapp.data.api

import com.example.foodieapp.data.api.dto.AppResponseDTO
import com.example.foodieapp.data.api.dto.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("user/sign-in")
    fun signIn(@Body body: HashMap<String, Any>): Call<AppResponseDTO<UserDTO>>
}