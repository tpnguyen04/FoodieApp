package com.example.foodieapp.utils

import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.foodieapp.data.model.Cart

object BadgeUtils {
    fun updateBadge(textBadge: TextView?, cart: Cart?) {
        val totalProduct = cart?.listProduct?.size ?: 0

        if (totalProduct == 0) {
            textBadge?.isGone = true
        } else {
            textBadge?.isVisible = true
            val totalQuantity = cart?.listProduct
                ?.map { it?.quantity ?: 0 }
                ?.reduce { acc, quantity -> acc + quantity }
                .toString()
            textBadge?.text = totalQuantity
        }
    }
}