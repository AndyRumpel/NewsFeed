package com.arsoft.newsfeed.data.video.request

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
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
    ): Deferred<VideoPlayerResponse>


    companion object Factory {
        fun create(): VideoPlayerService {
            val okHttpClient = OkHttpClient().newBuilder()
                .protocols(mutableListOf(Protocol.HTTP_1_1))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.vk.com/method/")
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(VideoPlayerService::class.java)
        }
    }

}