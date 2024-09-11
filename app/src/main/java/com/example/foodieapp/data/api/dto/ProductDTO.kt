package com.example.foodieapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class ProductDTO(
    @SerializedName("_id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("address")
    val address: String?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("img")
    val image: String?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("gallery")
    val gallery: List<String>?,
    @SerializedName("date_created")
    val dateCreated: String?,
    @SerializedName("date_updated")
    val dateUpdated: String?,
    @SerializedName("__v")
    val v: Int?,
)
