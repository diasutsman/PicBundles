package com.diasandfahri.picbundles.data.response

import com.google.gson.annotations.SerializedName

data class RelatedResponse(
    @field:SerializedName("related_collections")
    val relatedCollections: RelatedCollections,

    @field:SerializedName("user")
    val user: User? = null,


    @field:SerializedName("links")
    val links: Links? = null,
)

data class RelatedCollections(
    @field:SerializedName("results")
    val results: List<Result>? = null,
)

data class Result(
    @field:SerializedName("preview_photos")
    val previewPhotos: List<PhotoItem>? = null,
)