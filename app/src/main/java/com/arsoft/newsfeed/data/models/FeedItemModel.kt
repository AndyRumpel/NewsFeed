package com.arsoft.newsfeed.data.models

data class FeedItemModel(
    val avatar: String,
    val sourceName: String,
    val postText: String,
    val attachments: ArrayList<IAttachment>,
    val date: String
)