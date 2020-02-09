package com.arsoft.newsfeed.data.video.repository

import android.util.Log
import com.arsoft.newsfeed.data.video.request.VideoPlayerService

class VideoPlayerRepository(private val apiService: VideoPlayerService) {

    private val VERSION = "5.103"

    suspend fun getVideoLink(ownerID: String, videoID: String, accessToken: String): String {
        val result = apiService.getVideo(
            ownerID = ownerID,
            videoID = videoID,
            accessToken = accessToken,
            version = VERSION
        ).await()

        return result.response.items.first().files.getMax()

    }
}