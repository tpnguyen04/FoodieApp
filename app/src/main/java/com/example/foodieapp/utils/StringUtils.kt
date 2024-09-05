package com.example.foodieapp.utils

import java.text.DecimalFormat

object StringUtils {
    fun formatCurrency(number: Int): String {
        return DecimalFormat("#,###").format(number)
    }
}