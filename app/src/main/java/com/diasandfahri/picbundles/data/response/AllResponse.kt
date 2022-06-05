package com.diasandfahri.picbundles.data.response

import android.os.Parcelable
import com.diasandfahri.picbundles.data.entity.PhotoEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoItem(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("urls")
    val urls: Urls? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("user")
    val user: User? = null,

    @field:SerializedName("links")
    val links: Links? = null,
) : Parcelable {
    fun asPhotoEntity() = PhotoEntity(
        id = id as String,
        createdAt = createdAt as String,
        regularImageUrl = urls?.regular as String,
        userFullname = user?.fullName ?: "",
        userProfileImageUrl = user?.profileImage?.small ?: ""
    )
}

@Parcelize
data class Links(

    @field:SerializedName("download")
    val download: String? = null,
) : Parcelable

@Parcelize
data class User(
    @field:SerializedName("first_name")
    val firstName: String? = null,
    @field:SerializedName("last_name")
    val lastName: String? = null,
    @field:SerializedName("profile_image")
    val profileImage: ProfileImage? = null,
) : Parcelable {
    val fullName: String
        get() = "$firstName${lastName?.let { " $it" } ?: ""}"
}

@Parcelize
data class Urls(
    @field:SerializedName("regular")
    val regular: String? = null,
) : Parcelable

@Parcelize
data class ProfileImage(

    @field:SerializedName("small")
    val small: String? = null,

    @field:SerializedName("large")
    val large: String? = null,

    @field:SerializedName("medium")
    val medium: String? = null,
) : Parcelable
