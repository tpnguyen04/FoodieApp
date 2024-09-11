package com.example.foodieapp.data.repository

import com.example.foodieapp.common.AppInterface
import com.example.foodieapp.data.api.RetrofitClient
import com.example.foodieapp.data.api.dto.AppResponseDTO
import com.example.foodieapp.data.api.dto.ProductDTO
import com.example.foodieapp.data.enum.StatusCodeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProductRepository {
    private val apiService by lazy {
        RetrofitClient.getApiService()
    }

    fun getProductList(onListenResponse: AppInterface.OnListenResponse<List<ProductDTO>>) {
        CoroutineScope(Dispatchers.IO).launch {
            apiService.getProductListService()
                .enqueue(object : Callback<AppResponseDTO<List<ProductDTO>>> {
                    override fun onFailure(
                        call: Call<AppResponseDTO<List<ProductDTO>>>,
                        t: Throwable
                    ) {
                        onListenResponse.onFail(t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<AppResponseDTO<List<ProductDTO>>>,
                        response: Response<AppResponseDTO<List<ProductDTO>>>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            onListenResponse.onSuccess(response.body()?.data)
                        } else if (response.errorBody() != null && StatusCodeType.hasCodeError(response.code())) {
                            val json = JSONObject(response.body()?.toString() ?: "{}")
                            val errorMessage = json.optString("message")
                            onListenResponse.onFail(errorMessage)
                        }
                     }
                })
        }
    }
}