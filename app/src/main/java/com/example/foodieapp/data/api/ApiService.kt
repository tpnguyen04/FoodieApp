package com.example.foodieapp.data.api

import com.example.foodieapp.data.api.dto.AppResponseDTO
import com.example.foodieapp.data.api.dto.CartDTO
import com.example.foodieapp.data.api.dto.ProductDTO
import com.example.foodieapp.data.api.dto.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiService {
    @POST("user/sign-in")
    fun logIn(@Body body: HashMap<String, Any>): Call<AppResponseDTO<UserDTO>>

    @POST("user/sign-up")
    fun signUp(@Body body: HashMap<String, Any>): Call<AppResponseDTO<UserDTO>>

    @GET("product")
    fun getProductListService(): Call<AppResponseDTO<List<ProductDTO>>>

//    @GET("cart")
//    fun getCartService(@Header("Authorization") token: String): Call<AppResponseDTO<CartDTO>>

    @GET("cart")
    fun getCartService(): Call<AppResponseDTO<CartDTO>>

    @POST("cart/add")
    fun addCartService(@Body body: HashMap<String, Any>): Call<AppResponseDTO<CartDTO>>

    @POST("cart/update")
    fun updateCartService(@Body body: HashMap<String, Any>): Call<AppResponseDTO<CartDTO>>

    @POST("cart/conform")
    fun confirmCartService(@Body body: HashMap<String, Any>): Call<AppResponseDTO<String>>

    @POST("order/history")
    fun getHistoryService(): Call<AppResponseDTO<List<CartDTO>>>
}