package com.diasandfahri.picbundles.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diasandfahri.picbundles.data.network.ApiConfig
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.data.response.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoViewModel : ViewModel() {

    val imagesList = MutableLiveData<List<PhotoItem>?>()
    val isLoading = MutableLiveData(true)
    val isError = MutableLiveData<Throwable?>()

    val relatedImageList = MutableLiveData<List<PhotoItem>?>()
    val currentUser = MutableLiveData<User?>()
    val isRelatedLoading = MutableLiveData(true)
    val isRelatedError = MutableLiveData<Throwable?>()


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

    fun getRelatedPhotosById(id: String) {
        loadData(
            ApiConfig.getApiService().getRelatedPhotosById(id),
            { relatedResponse ->
                val results = arrayListOf<PhotoItem>()
                relatedResponse.relatedCollections.results?.forEach {
                    it.previewPhotos?.let { it1 -> results.addAll(it1) }
                }
                relatedImageList.value = results
                isRelatedError.value = null
                isRelatedLoading.value = false
                currentUser.value = relatedResponse.user
            },
            {
                relatedImageList.value = null
                isRelatedError.value = it
                isRelatedLoading.value = false
                currentUser.value = null
            }
        )
    }


}