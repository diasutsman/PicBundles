package com.diasandfahri.picbundles.ui

import android.app.Application
import androidx.lifecycle.*
import com.diasandfahri.picbundles.data.network.ApiConfig
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.data.response.User
import com.diasandfahri.picbundles.data.room.PhotoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val photoDao = PhotoDatabase.getDatabase(application).photoDao()

    // image item in recycler view
    val imagesList = MutableLiveData<List<PhotoItem>?>()
    val isLoading = MutableLiveData(true)
    val isError = MutableLiveData<Throwable?>()

    // Detail
    val relatedImageList = MutableLiveData<List<PhotoItem>?>()
    val currentUser = MutableLiveData<User?>()
    val isRelatedLoading = MutableLiveData(true)
    val isRelatedError = MutableLiveData<Throwable?>()

    // search
    val searchList = MutableLiveData<List<PhotoItem>?>()
    val isSearchLoading = MutableLiveData(false)
    val isSearchError = MutableLiveData<Throwable?>()

    // Remote
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

    fun searchPhotoByQuery(query: String) {
        isSearchLoading.value = true
        loadData(
            ApiConfig.getApiService().sesrchPhotoByQuery(query),
            {
                searchList.value = it.results
                isSearchError.value = null
                isSearchLoading.value = false
            },
            {
                searchList.value = null
                isSearchError.value = it
                isSearchLoading.value = false
            }
        )
    }

    // DAO

    fun bookmarkPhoto(photo: PhotoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            photoDao.insertPhoto(photo.asPhotoEntity())
        }
    }

    fun unBookmarkPhoto(photo: PhotoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            photoDao.deletePhoto(photo.asPhotoEntity())
        }
    }

    fun isBookmarked(photo: PhotoItem): LiveData<Boolean> {
        return Transformations.map(photoDao.isPhotoBookmarked(photo.id as String)) {
            it == 1
        }
    }

    fun getAllBookmarkedPhotos(): LiveData<List<PhotoItem>> {
        return Transformations.map(photoDao.getAllBookmarkedPhotos()) { list ->
            list.map { it.asPhotoResponse() }
        }
    }

    fun saveUserIfNotExist(photo: PhotoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            photoDao.editPhoto(photo.asPhotoEntity())
        }
    }
}