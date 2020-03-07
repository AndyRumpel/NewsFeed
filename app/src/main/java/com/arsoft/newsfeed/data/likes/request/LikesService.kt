package com.arsoft.newsfeed.data.likes.request

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface LikesService {

    @GET(value = "likes.add")
    fun addLike(
        @Query(value = "type") type: String,
        @Query(value = "owner_id") ownerId: Long,
        @Query(value = "item_id") itemId: Long,
        @Query(value = "access_token") accessToken: String,
        @Query(value = "v") version: String
    ): Deferred<LikesResponse>

    @GET(value = "likes.delete")
    fun deleteLike(
        @Query(value = "type") type: String,
        @Query(value = "owner_id") ownerId: Long,
        @Query(value = "item_id") itemId: Long,
        @Query(value = "access_token") accessToken: String,
        @Query(value = "v") version: String
    ): Deferred<LikesResponse>
}