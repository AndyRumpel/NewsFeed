package com.arsoft.newsfeed.data.comments.request

data class CreateCommentResponse(
    val response: CreateResponse
)

data class CreateResponse(
    val commentId: Long,
    val parentStack: ArrayList<Long>
)