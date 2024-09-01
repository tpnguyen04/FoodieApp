package com.example.foodieapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("email")
    val email: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("userGroup")
    val userGroup: Int?,
    @SerializedName("registerDate")
    val registerDate: String?,
    @SerializedName("token")
    val token: String?
)
