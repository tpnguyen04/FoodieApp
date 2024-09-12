package com.example.foodieapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class CartDTO(
    @SerializedName("_id")
    var id: String?,
    @SerializedName("products")
    var listProductDTO: List<ProductDTO>?,
    @SerializedName("id_user")
    var idUser: String?,
    @SerializedName("price")
    var price: Long?,
    @SerializedName("date_created")
    var dateCreated: String?
)
