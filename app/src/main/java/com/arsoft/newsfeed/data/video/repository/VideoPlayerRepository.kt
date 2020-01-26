package com.arsoft.newsfeed.data.video.repository

import android.util.Log
import com.arsoft.newsfeed.data.video.request.VideoPlayerResponse
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

        when {
            result.response.items.first().files.mp4_720 != null -> {
                Log.e("VIDEO", result.response.items.first().files.mp4_720!!)
                return result.response.items.first().files.mp4_720!!
            }
            result.response.items.first().files.mp4_480 != null -> {
                Log.e("VIDEO", result.response.items.first().files.mp4_480!!)
                return result.response.items.first().files.mp4_480!!
            }
            result.response.items.first().files.mp4_360 != null -> {
                Log.e("VIDEO", result.response.items.first().files.mp4_360!!)
                return result.response.items.first().files.mp4_360!!
            }
            result.response.items.first().files.mp4_240 != null -> {
                Log.e("VIDEO", result.response.items.first().files.mp4_240!!)
                return result.response.items.first().files.mp4_240!!
            }
            else -> return ""
        }

    }
}