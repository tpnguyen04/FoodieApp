package com.example.foodieapp.data.repository

import com.example.foodieapp.common.AppInterface
import com.example.foodieapp.data.api.RetrofitClient
import com.example.foodieapp.data.api.dto.AppResponseDTO
import com.example.foodieapp.data.api.dto.UserDTO
import com.example.foodieapp.data.enum.StatusCodeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AuthenticationRepository {
    private val apiService by lazy {
        RetrofitClient.getApiService()
    }

    fun requestLogIn(
        email: String,
        password: String,
        onListenResponse: AppInterface.OnListenResponse<UserDTO>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val hashMap = HashMap<String, Any>().apply {
                put("email", email)
                put("password", password)
            }
            apiService.logIn(hashMap).enqueue(object : Callback<AppResponseDTO<UserDTO>>{
                override fun onFailure(call: Call<AppResponseDTO<UserDTO>>, t: Throwable) {
                    onListenResponse.onFail(t.message.toString())
                }

                override fun onResponse(
                    call: Call<AppResponseDTO<UserDTO>>,
                    response: Response<AppResponseDTO<UserDTO>>
                ) {
                    // If success
                    if (response.isSuccessful && response.body() != null) {
                        onListenResponse.onSuccess(response.body()?.data)
                    } else if (response.errorBody() != null && StatusCodeType.hasCodeError(response.code())) {
                        val json = JSONObject(response.errorBody()?.string() ?: "{}")
                        val errorMessage = json.optString("message")
                        onListenResponse.onFail(errorMessage)
                    }
                }
            })
        }
    }

    fun requestSignUp(
        email: String,
        password: String,
        name: String,
        phone: String,
        address: String,
        onListenResponse: AppInterface.OnListenResponse<UserDTO>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val hashMap = HashMap<String, Any>().apply {
                put("email", email)
                put("password", password)
                put("name", name)
                put("phone", phone)
                put("address", address)
            }

            apiService.signUp(hashMap).enqueue(object: Callback<AppResponseDTO<UserDTO>> {
                override fun onFailure(call: Call<AppResponseDTO<UserDTO>>, t: Throwable) {
                    onListenResponse.onFail(t.message.toString())
                }

                override fun onResponse(
                    call: Call<AppResponseDTO<UserDTO>>,
                    response: Response<AppResponseDTO<UserDTO>>
                ) {
                    if(response.isSuccessful && response.body() != null) {
                        onListenResponse.onSuccess(response.body()?.data)
                    } else if (response.errorBody() != null && StatusCodeType.hasCodeError(response.code())) {
                       val json = JSONObject(response.errorBody()?.string() ?: "{}")
                        val errorMessage = json.optString("message")
                        onListenResponse.onFail(errorMessage)
                    }
                }
            })

        }
    }
}