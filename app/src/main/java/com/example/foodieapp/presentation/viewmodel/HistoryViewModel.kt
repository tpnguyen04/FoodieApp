package com.example.foodieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodieapp.common.AppInterface
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.data.api.dto.CartDTO
import com.example.foodieapp.data.model.Cart
import com.example.foodieapp.data.repository.HistoryRepository
import com.example.foodieapp.helper.CartHelper
import kotlinx.coroutines.launch

class HistoryViewModel: ViewModel() {
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val historyLiveData = MutableLiveData<AppResource<List<Cart?>>>()

    private val historyRepository = HistoryRepository

    fun getLoading(): LiveData<Boolean> = loadingLiveData
    fun getHistoryLiveData(): LiveData<AppResource<List<Cart?>>> = historyLiveData

    fun getHistory(token: String) {
        loadingLiveData.value = true
        viewModelScope.launch {
            historyRepository.getHistory(token, object: AppInterface.OnListenResponse<List<CartDTO>> {
                override fun onFail(message: String) {
                    historyLiveData.postValue(AppResource.Error(message))
                    loadingLiveData.value = false
                }

                override fun onSuccess(data: List<CartDTO>?) {
                    val listCart = data?.map {
                        CartHelper.convertToCart(it)
                    }
                    historyLiveData.postValue(AppResource.Success(listCart))
                    loadingLiveData.value = false
                }
            })
        }
    }
}