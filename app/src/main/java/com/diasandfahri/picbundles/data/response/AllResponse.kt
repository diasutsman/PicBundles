package com.diasandfahri.picbundles.data.response

import com.google.gson.annotations.SerializedName

data class PhotoItem(


	@field:SerializedName("urls")
	val urls: Urls? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,
)

data class Urls(
	@field:SerializedName("regular")
	val regular: String? = null,
)

data class ProfileImage(

	@field:SerializedName("small")
	val small: String? = null,

	@field:SerializedName("large")
	val large: String? = null,

	@field:SerializedName("medium")
	val medium: String? = null
)
