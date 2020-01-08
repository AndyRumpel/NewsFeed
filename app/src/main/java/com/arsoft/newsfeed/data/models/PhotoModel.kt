package com.arsoft.newsfeed.data.models

data class PhotoModel(
    val id: Long,
    val photo_sizes: List<PhotoSizesModel>
)