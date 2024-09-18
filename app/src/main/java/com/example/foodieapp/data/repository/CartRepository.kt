package com.example.foodieapp.data.repository

import com.example.foodieapp.common.AppInterface
import com.example.foodieapp.data.api.ApiService
import com.example.foodieapp.data.api.RetrofitClient.createRetrofitWithToken
import com.example.foodieapp.data.api.dto.AppResponseDTO
import com.example.foodieapp.data.api.dto.CartDTO
import com.example.foodieapp.data.enum.StatusCodeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

object CartRepository {

//    private val apiService: ApiService by lazy {
//        RetrofitClient.getApiService()
//    }

    fun getCart(
        token: String,
        onListenResponse: AppInterface.OnListenResponse<CartDTO>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val retrofit: Retrofit = createRetrofitWithToken(token)
            val apiService = retrofit.create(ApiService::class.java)
            apiService.getCartService().enqueue(object: Callback<AppResponseDTO<CartDTO>> {
                override fun onFailure(call: Call<AppResponseDTO<CartDTO>>, t: Throwable) {
                    onListenResponse.onFail(t.message.toString())
                }

                override fun onResponse(
                    call: Call<AppResponseDTO<CartDTO>>,
                    response: Response<AppResponseDTO<CartDTO>>
                ) {
                    if ((response.isSuccessful && response.body() != null || StatusCodeType.ERROR_CODE_500.code == response.code())) {
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