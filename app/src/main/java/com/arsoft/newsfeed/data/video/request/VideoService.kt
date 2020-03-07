package com.arsoft.newsfeed.data.video.request

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoService {

    @GET(value = "video.get")
    fun getVideo(
        @Query(value = "owner_id")ownerID: Long,
        @Query(value = "videos")videoID: String,
        @Query(value = "access_token")accessToken: String,
        @Query(value = "v")version: String
    ): Deferred<VideoResponse>
}