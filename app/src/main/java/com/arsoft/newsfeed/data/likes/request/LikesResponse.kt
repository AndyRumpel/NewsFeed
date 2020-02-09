package com.arsoft.newsfeed.data.likes.request

data class LikesResponse(
    val response: Response
)

data class Response(
    val like: Int
)