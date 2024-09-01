package com.example.foodieapp.common

object AppInterface {
    interface OnListenResponse<T> {
        fun onSuccess(data: T?)
        fun onFail(message: String)
    }
}