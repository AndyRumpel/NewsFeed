package com.arsoft.newsfeed.data.video.request

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoPlayerService {

    @GET(value = "video.get")
    fun getVideo(
        @Query(value = "owner_id")ownerID: String,
        @Query(value = "videos")videoID: String,
        @Query(value = "access_token")accessToken: String,
        @Query(value = "v")version: String
    ): Single<VideoPlayerResponse>


    companion object Factory {
        fun create(): VideoPlayerService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.vk.com/method/")
                .build()

            return retrofit.create(VideoPlayerService::class.java)
        }
    }

}