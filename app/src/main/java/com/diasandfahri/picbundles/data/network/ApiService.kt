package com.diasandfahri.picbundles.data.network

import com.diasandfahri.picbundles.BuildConfig
import com.diasandfahri.picbundles.data.response.PhotoItem
import com.diasandfahri.picbundles.data.response.RelatedResponse
import com.diasandfahri.picbundles.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("photos")
    fun getAllPhotos(
        @Query("page") page: Int = 1,
        @Query("client_id") clientId: String = BuildConfig.API_KEY,
        @Query("per_page") perPage: Int = 30,
        @Query("content_filter") contentFilter: String = "high",
    ): Call<List<PhotoItem>>

    @GET("photos/{id}")
    fun getRelatedPhotosById(
        @Path("id") id: String,
        @Query("client_id") clientId: String = BuildConfig.API_KEY,
        @Query("content_filter") contentFilter: String = "high",
    ): Call<RelatedResponse>

    @GET("search/photos")
    fun sesrchPhotoByQuery(
        @Query("query") query: String,
        @Query("client_id") clientId: String = BuildConfig.API_KEY,
        @Query("per_page") perPage: Int = 30,
        @Query("content_filter") contentFilter: String = "high",
    ): Call<SearchResponse>

}