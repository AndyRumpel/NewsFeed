package com.arsoft.newsfeed.data.models

data class CommentModel(
    val name: String,
    val avatar: String,
    val date: Long,
    val text: String,
    val attachments: ArrayList<IAttachment>,
    val thread: ArrayList<CommentModel>,
    val likesCount: Int,
    val userLikes: Int,
    val ownerId: Long,
    val postId: Long,
    val itemId: Long,
    var isFavorite: Boolean
)