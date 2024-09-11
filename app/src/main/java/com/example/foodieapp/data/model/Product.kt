package com.example.foodieapp.data.model

data class Product(
    val id: String?,
    val name: String?,
    val address: String?,
    val price: Int?,
    val image: String?,
    val quantity: Int?,
    val gallery: List<String>?
)
