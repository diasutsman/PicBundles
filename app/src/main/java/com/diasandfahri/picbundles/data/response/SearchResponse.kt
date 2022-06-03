package com.diasandfahri.picbundles.data.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("results")
    val results: List<PhotoItem>
)
