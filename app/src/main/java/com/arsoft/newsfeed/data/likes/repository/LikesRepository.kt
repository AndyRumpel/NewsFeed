package com.arsoft.newsfeed.data.likes.repository

import com.arsoft.newsfeed.data.likes.request.LikesResponse
import com.arsoft.newsfeed.data.likes.request.LikesService

class LikesRepository(private val apiService: LikesService) {

    private val VERSION = "5.103"

    suspend fun addLike(
            type: String,
            ownerId: Long,
            itemId: Long,
            accessToken: String): LikesResponse{

        return apiService.addLike(
            type = type,
            ownerId = ownerId,
            itemId = itemId,
            accessToken = accessToken,
            version = VERSION
        ).await()
    }

    suspend fun deleteLike(
        type: String,
        ownerId: Long,
        itemId: Long,
        accessToken: String): LikesResponse{

        return apiService.deleteLike(
            type = type,
            ownerId = ownerId,
            itemId = itemId,
            accessToken = accessToken,
            version = VERSION
        ).await()
    }

}