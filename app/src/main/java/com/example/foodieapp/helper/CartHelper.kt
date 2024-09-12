package com.example.foodieapp.helper

import com.example.foodieapp.data.api.dto.CartDTO
import com.example.foodieapp.data.model.Cart

object CartHelper {
    fun parseCartDTO(cartDTO: CartDTO?): Cart {
        return Cart(
            id = cartDTO?.id ?: "",
            idUser = cartDTO?.idUser ?: "",
            price = cartDTO?.price ?: 0,
            dateCreated = cartDTO?.dateCreated ?: "",
            listProduct = cartDTO?.listProductDTO?.map { ProductHelper.convertToProduct(it) } ?: emptyList()
        )
    }
}