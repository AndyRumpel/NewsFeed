package com.arsoft.newsfeed.data.models

data class CommentModel(
    val name: String,
    val avatar: String,
    val date: Long,
    val text: String,
    val attachments: ArrayList<IAttachment>
)