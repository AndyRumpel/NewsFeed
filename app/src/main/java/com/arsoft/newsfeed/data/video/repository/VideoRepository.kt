package com.arsoft.newsfeed.data.video.repository

import com.arsoft.newsfeed.data.video.request.VideoService

class VideoRepository(private val apiService: VideoService) {

    private val VERSION = "5.103"

    suspend fun getVideoLink(ownerID: Long, videoID: String, accessToken: String, platform: String): String {
        val result = apiService.getVideo(
            ownerID = ownerID,
            videoID = videoID,
            accessToken = accessToken,
            version = VERSION
        ).await()

        return if (platform == "YouTube") {
            result.response.items.first().player!!
        } else {
            result.response.items.first().files.getMax()
        }
    }
}