package com.arsoft.newsfeed.data.likes.repository

import android.util.Log
import com.arsoft.newsfeed.data.likes.request.LikesResponse
import com.arsoft.newsfeed.data.likes.request.LikesService

class LikesRepository(private val apiService: LikesService) {

    private val TYPE_POST = "post"
    private val VERSION = "5.103"

    suspend fun addLike(
            ownerId: Long,
            itemId: Long,
            accessToken: String): LikesResponse{

        return apiService.addLike(
            type = TYPE_POST,
            ownerId = ownerId,
            itemId = itemId,
            accessToken = accessToken,
            version = VERSION
        ).await()
    }

    suspend fun deleteLike(
        ownerId: Long,
        itemId: Long,
        accessToken: String): LikesResponse{

        return apiService.deleteLike(
            type = TYPE_POST,
            ownerId = ownerId,
            itemId = itemId,
            accessToken = accessToken,
            version = VERSION
        ).await()
    }

}