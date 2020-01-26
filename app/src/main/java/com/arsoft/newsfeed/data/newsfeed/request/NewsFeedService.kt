package com.arsoft.newsfeed.data.newsfeed.request

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsFeedService {

    @GET(value = "newsfeed.get")
    fun getNewsFeed(
        @Query(value = "count") count: Int,
        @Query(value = "access_token") accessToken: String,
        @Query(value = "v") version: String,
        @Query(value = "filters") filters: String
    ): Deferred<NewsFeedResponse>

    companion object Factory {
        fun create(): NewsFeedService {
            val okHttpClient = OkHttpClient().newBuilder()
                .protocols(mutableListOf(Protocol.HTTP_1_1))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.vk.com/method/")
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(NewsFeedService::class.java)
        }
    }
}