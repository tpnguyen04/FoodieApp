package com.example.foodieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodieapp.common.AppInterface
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.data.api.dto.ProductDTO
import com.example.foodieapp.data.model.Product
import com.example.foodieapp.data.repository.ProductRepository
import com.example.foodieapp.helper.ProductHelper
import kotlinx.coroutines.launch

class ProductViewModel: ViewModel() {

    private val loadingLiveData = MutableLiveData<Boolean>()
    private val productLiveData = MutableLiveData<AppResource<List<Product?>>>()

    private var productRepository = ProductRepository

    fun getLoading(): LiveData<Boolean> = loadingLiveData
    fun getProductLiveData(): LiveData<AppResource<List<Product?>>> = productLiveData

    fun getProductList() {
        loadingLiveData.value = true
        viewModelScope.launch {
            productRepository.getProductList(onListenResponse = object : AppInterface.OnListenResponse<List<ProductDTO>> {
                override fun onFail(message: String) {
                    AppResource.Error(message)
                    loadingLiveData.value = false
                }

                override fun onSuccess(data: List<ProductDTO>?) {
                    val listProduct = data?.map {
                        ProductHelper.convertToProduct(it)
                    }
                    listProduct?.let {
                        productLiveData.postValue(AppResource.Success(it))
                    }
                    loadingLiveData.value = false
                }
            })
        }
    }
}