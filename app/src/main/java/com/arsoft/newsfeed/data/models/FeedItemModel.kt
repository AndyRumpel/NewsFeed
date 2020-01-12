package com.arsoft.newsfeed.data.models

data class FeedItemModel(
    val avatar: String,
    val sourceName: String,
    val postText: String,
    val photoURLs: ArrayList<String>?,
    val videoPreviewImage: String?,
    val videoDuration: Int?,
    val videoID: String?,
    val videoOwnerID: String?,
    val date: String
)