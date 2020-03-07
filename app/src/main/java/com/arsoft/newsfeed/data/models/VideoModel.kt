package com.arsoft.newsfeed.data.models

data class VideoModel(
    val previewImage: String,
    val duration: Int,
    val videoID: String,
    val ownerID: Long,
    val platform: String,
    val title: String
): IAttachment