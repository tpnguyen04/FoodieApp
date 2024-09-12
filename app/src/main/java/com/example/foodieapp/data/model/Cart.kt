package com.example.foodieapp.data.model

data class Cart(
    val id: String?,
    val listProduct: List<Product?>,
    var idUser: String?,
    var price: Long?,
    var dateCreated: String?,
)
