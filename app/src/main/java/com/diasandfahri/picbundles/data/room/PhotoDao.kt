package com.diasandfahri.picbundles.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.diasandfahri.picbundles.data.entity.PhotoEntity

@Dao
interface PhotoDao {

    /**
    to insert photo data that has been bookmarked
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPhoto(photo: PhotoEntity)

    /**
     * to get all photos that has been bookmarked
     */
    @Query("SELECT * FROM photos")
    fun getAllBookmarkedPhotos(): LiveData<List<PhotoEntity>>

    /**
     * to unbookmarked photo
     */
    @Delete
    suspend fun deletePhoto(photo: PhotoEntity)

    /**
     * check if photo has been bookmarked
     * will return 1 if photo has been bookmarked
     * will return 0 if photo has not been bookmarked
     */
    @Query("SELECT COUNT(1) FROM photos WHERE id = :id")
    fun isPhotoBookmarked(id: String): LiveData<Int>

    /**
     * edit photo by id
     * this will be used in detail when the user data is not provided
     * so that it will saved the user data to the room
     */
    @Update
    suspend fun editPhoto(photo: PhotoEntity)
}