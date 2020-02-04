package com.arsoft.newsfeed.data.models

data class VideoModel(
    val videoPreviewImage: String?,
    val videoDuration: Int?,
    val videoID: String?,
    val videoOwnerID: String?
): IAttachment