package com.example.foodieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodieapp.common.AppInterface
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.data.api.dto.CartDTO
import com.example.foodieapp.data.api.dto.ProductDTO
import com.example.foodieapp.data.model.Cart
import com.example.foodieapp.data.model.Product
import com.example.foodieapp.data.repository.CartRepository
import com.example.foodieapp.data.repository.ProductRepository
import com.example.foodieapp.helper.CartHelper
import com.example.foodieapp.helper.ProductHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel: ViewModel() {

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val productLiveData = MutableLiveData<AppResource<List<Product?>>>()
    private val cartLiveData = MutableLiveData<AppResource<Cart?>>()

    private val productRepository = ProductRepository
    private val cartRepository = CartRepository

    fun getLoading(): LiveData<Boolean> = loadingLiveData
    fun getProductLiveData(): LiveData<AppResource<List<Product?>>> = productLiveData
    fun getCartLiveData(): LiveData<AppResource<Cart?>> = cartLiveData

    fun updateCartLiveData(token: String) {
        viewModelScope.launch {
            delay(500)
            cartRepository.getCart(token, object : AppInterface.OnListenResponse<CartDTO> {
                override fun onFail(message: String) {
                    cartLiveData.postValue(AppResource.Error(message))
                    loadingLiveData.value = false
                }

                override fun onSuccess(data: CartDTO?) {
                    val cart = CartHelper.convertToCart(data)
                    cartLiveData.postValue(AppResource.Success(cart))
                    loadingLiveData.value = false
                }
            })
        }
    }

    fun updateProductList() {
        loadingLiveData.value = true
        viewModelScope.launch {
            delay(500)
            productRepository.getProductList(onListenResponse = object : AppInterface.OnListenResponse<List<ProductDTO>> {
                override fun onFail(message: String) {
                    productLiveData.postValue(AppResource.Error(message))
                    loadingLiveData.value = false
                }

                override fun onSuccess(data: List<ProductDTO>?) {
                    val listProduct = data?.map {
                        ProductHelper.convertToProduct(it)
                    }
                    productLiveData.postValue(AppResource.Success(listProduct))
                    loadingLiveData.value = false
                }
            })
        }
    }

    fun getProductList() {
        loadingLiveData.value = true
        viewModelScope.launch {
            productRepository.getProductList(onListenResponse = object : AppInterface.OnListenResponse<List<ProductDTO>> {
                override fun onFail(message: String) {
                    productLiveData.postValue(AppResource.Error(message))
                    loadingLiveData.value = false
                }

                override fun onSuccess(data: List<ProductDTO>?) {
                    val listProduct = data?.map {
                        ProductHelper.convertToProduct(it)
                    }
                    productLiveData.postValue(AppResource.Success(listProduct))
                    loadingLiveData.value = false
                }
            })
        }
    }

    fun getCart(token: String) {

        loadingLiveData.value = true
        viewModelScope.launch {
            cartRepository.getCart(token, object : AppInterface.OnListenResponse<CartDTO> {
                override fun onFail(message: String) {
                    cartLiveData.postValue(AppResource.Error(message))
                    loadingLiveData.value = false
                }

                override fun onSuccess(data: CartDTO?) {
                    val cart = CartHelper.convertToCart(data)
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
                    val cart = CartHelper.convertToCart(data)
                    cartLiveData.postValue(AppResource.Success(cart))
                    loadingLiveData.value = false
                }
            })
        }
    }
}