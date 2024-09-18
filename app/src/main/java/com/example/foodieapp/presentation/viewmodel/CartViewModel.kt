package com.example.foodieapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodieapp.common.AppCommon
import com.example.foodieapp.common.AppInterface
import com.example.foodieapp.common.AppSharedPreferences
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.data.api.dto.CartDTO
import com.example.foodieapp.data.model.Cart
import com.example.foodieapp.data.repository.CartRepository
import com.example.foodieapp.helper.CartHelper
import kotlinx.coroutines.launch

class CartViewModel: ViewModel() {
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val cartLiveData = MutableLiveData<AppResource<Cart?>>()

    private val cartRepository = CartRepository

    fun getLoading(): LiveData<Boolean> = loadingLiveData

    fun getCartLiveData(): LiveData<AppResource<Cart?>> = cartLiveData

    fun getCart(token: String) {

        loadingLiveData.value = true
        viewModelScope.launch {
            cartRepository.getCart(token, object : AppInterface.OnListenResponse<CartDTO> {
                override fun onFail(message: String) {
                    cartLiveData.postValue(AppResource.Error(message))
                    loadingLiveData.value = false
                }

                override fun onSuccess(data: CartDTO?) {
                    val cart = CartHelper.parseCartDTO(data)
                    cartLiveData.postValue(AppResource.Success(cart))
                    loadingLiveData.value = false
                }
            })
        }
    }

    fun addCart(token: String, idProduct: String) {

        loadingLiveData.value = true
        viewModelScope.launch {
            cartRepository.addCart(token, idProduct, object : AppInterface.OnListenResponse<CartDTO> {
                override fun onFail(message: String) {
                    cartLiveData.postValue(AppResource.Error(message))
                    loadingLiveData.value = false
                }

                override fun onSuccess(data: CartDTO?) {
                    val cart = CartHelper.parseCartDTO(data)
                    cartLiveData.postValue(AppResource.Success(cart))
                    loadingLiveData.value = false
                }
            })
        }
    }
}