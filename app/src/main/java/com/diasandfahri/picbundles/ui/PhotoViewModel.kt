package com.diasandfahri.picbundles.ui

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.diasandfahri.picbundles.R
import com.diasandfahri.picbundles.data.network.ApiConfig
import com.diasandfahri.picbundles.data.response.Links
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.data.response.RelatedResponse
import com.diasandfahri.picbundles.data.response.User
import com.diasandfahri.picbundles.data.room.PhotoDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val photoDao = PhotoDatabase.getDatabase(application).photoDao()

    // image item in recycler view
    val imagesList = MutableLiveData<List<PhotoItem>?>()
    val isLoading = MutableLiveData(true)
    val isError = MutableLiveData<Throwable?>()
    private var page = 1

    // Detail
    val relatedImageList = MutableLiveData<List<PhotoItem>?>()
    val currentRelatedResponse = MutableLiveData<RelatedResponse?>()
    val isRelatedLoading = MutableLiveData(true)
    val isRelatedError = MutableLiveData<Throwable?>()

    // search
    val searchList = MutableLiveData<List<PhotoItem>?>()
    val isSearchLoading = MutableLiveData(false)
    val isSearchError = MutableLiveData<Throwable?>()

    // Remote
    private fun <T : Any> loadData(
        apiCall: Flowable<T>,
        responseHandler: (T) -> Unit,
        errorHandler: (Throwable) -> Unit,
    ) {
        apiCall.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                responseHandler(response)
            }, { error ->
                errorHandler(error)
            })
    }

    fun getAllPhotos() {
        page = 1
        loadData(
            ApiConfig.getApiService().getAllPhotos(),
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
                currentRelatedResponse.value = relatedResponse
            },
            {
                relatedImageList.value = null
                isRelatedError.value = it
                isRelatedLoading.value = false
                currentRelatedResponse.value = null
            }
        )
    }

    fun searchPhotoByQuery(query: String) {
        isSearchLoading.value = true
        loadData(
            ApiConfig.getApiService().searchPhotoByQuery(query),
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

    fun downloadPhoto(context: Context, photo: PhotoItem) {
        photo.links?.download?.let {
            val request = DownloadManager.Request(Uri.parse(it))
            // allow wifi and mobile data download
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setTitle(context.getString(R.string.txt_download))
            request.setDescription("Downloading the file...")

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                photo.id.toString() + ".png"
            )

            // get download service and enqueue file
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)

            Toast.makeText(context, "Downloading ${photo.id}.png", Toast.LENGTH_SHORT).show()
        }
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