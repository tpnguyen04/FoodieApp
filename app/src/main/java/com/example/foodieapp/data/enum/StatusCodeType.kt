package com.example.foodieapp.data.enum

enum class StatusCodeType(var code: Int) {
    ERROR_CODE_400(400),

    ERROR_CODE_401(401),

    ERROR_CODE_403(403),

    ERROR_CODE_404(404),

    ERROR_CODE_500(500);

    companion object {
        fun hasCodeError(code: Int): Boolean {
            return entries.any { it.code == code }
        }
    }
}