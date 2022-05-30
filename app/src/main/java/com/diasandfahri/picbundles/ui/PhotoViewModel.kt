package com.diasandfahri.picbundles.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diasandfahri.picbundles.data.network.ApiConfig
import com.diasandfahri.picbundles.data.response.PhotoItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoViewModel : ViewModel() {

    val imagesList = MutableLiveData<List<PhotoItem>?>()
    val isLoading = MutableLiveData(true)
    val isError = MutableLiveData<Throwable?>()

    private fun <T> loadData(
        apiCall: Call<T>,
        onResponse: (T) -> Unit,
        onFailure: (Throwable) -> Unit,
    ) {
        apiCall.enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>,
            ) {
                response.body()?.let { onResponse(it) }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onFailure(t)
            }

        })
    }

    fun getAllPhotos(page: Int = 1) {
        loadData(
            ApiConfig.getApiService().getAllPhotos(page),
            {
                imagesList.value = it
                isError.value = null
                isLoading.value = false
            },
            {
                imagesList.value = null
                isError.value = it
                isLoading.value = false
            }
        )
    }


}