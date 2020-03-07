package com.arsoft.newsfeed.data.models

data class RepostModel(
    val avatar: String?,
    val sourceName: String?,
    val postText: String?,
    val attachments: ArrayList<IAttachment>,
    val date: Long
)