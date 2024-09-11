package com.example.foodieapp.helper

import com.example.foodieapp.data.api.dto.ProductDTO
import com.example.foodieapp.data.model.Product

object ProductHelper {

    fun convertToProduct(productDTO: ProductDTO?): Product? {
        productDTO ?: return null
        return Product(
            id = productDTO.id,
            name = productDTO.name,
            address = productDTO.address,
            price = productDTO.price,
            image = productDTO.image,
            quantity = productDTO.quantity,
            gallery = productDTO.gallery
        )
    }
}