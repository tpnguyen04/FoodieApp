package com.example.foodieapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodieapp.common.AppInterface
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.data.api.dto.UserDTO
import com.example.foodieapp.data.model.User
import com.example.foodieapp.data.repository.AuthenticationRepository
import com.example.foodieapp.helper.UserHelper
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val userLiveData = MutableLiveData<AppResource<User>>()

    fun getLoading(): LiveData<Boolean> = loadingLiveData
    fun getUser(): LiveData<AppResource<User>> = userLiveData

    private val repository = AuthenticationRepository

    fun login(email: String, password: String) {
        loadingLiveData.value = true
        viewModelScope.launch {
            repository.requestLogIn(email, password, object : AppInterface.OnListenResponse<UserDTO> {
                override fun onFail(message: String) {
                    userLiveData.postValue(AppResource.Error(message))
                    loadingLiveData.value = false
                }

                override fun onSuccess(data: UserDTO?) {
                    val user = UserHelper.convertToUser(data)
                    userLiveData.postValue(AppResource.Success(user))
                    loadingLiveData.value = false
                }
            })
        }

    }
}