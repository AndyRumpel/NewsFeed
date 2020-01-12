package com.arsoft.newsfeed.data.video.repository

import com.arsoft.newsfeed.data.video.request.VideoPlayerResponse
import com.arsoft.newsfeed.data.video.request.VideoPlayerService
import io.reactivex.Single

class VideoPlayerRepository(val apiService: VideoPlayerService) {

    fun getVideo(ownerID: String, videoID: String, accessToken: String, version: String): Single<VideoPlayerResponse> {
        return apiService.getVideo(
            ownerID = ownerID,
            videoID = videoID,
            accessToken = accessToken,
            version = version
        )
    }
}