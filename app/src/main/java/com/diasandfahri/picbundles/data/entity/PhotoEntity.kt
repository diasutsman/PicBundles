package com.diasandfahri.picbundles.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.diasandfahri.picbundles.data.response.*

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "image_url")
    val regularImageUrl: String,

    @ColumnInfo(name = "user_fullname")
    val userFullname: String,

    @ColumnInfo(name = "user_profile_image_url")
    val userProfileImageUrl: String,

    @ColumnInfo(name = "download_link")
    val downloadLink: String,
) {
    fun asPhotoResponse() = PhotoItem(
        id = id,
        createdAt = createdAt,
        urls = Urls(
            regular = regularImageUrl,
        ),
        user = User(
            firstName = userFullname.split(" ")[0],
            lastName = if (userFullname.split(" ").size == 1) null else userFullname.split(" ")[1],
            profileImage = ProfileImage(
                small = userProfileImageUrl,
            ),
        ),
        links = Links(
            download = downloadLink,
        ),
    )
}
