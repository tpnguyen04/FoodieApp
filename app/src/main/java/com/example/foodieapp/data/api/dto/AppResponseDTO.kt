package com.example.foodieapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class AppResponseDTO<T>(
    @SerializedName("data")
    var data: T? = null,
    @SerializedName("message")
    var message: String = ""
)
