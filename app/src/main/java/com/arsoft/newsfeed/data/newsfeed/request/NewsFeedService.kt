package com.arsoft.newsfeed.data.newsfeed.request

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsFeedService {

    @GET("newsfeed.get")
    fun getNewsFeed(
        @Query("count") count: Int,
        @Query("access_token") accessToken: String,
        @Query("v") version: String
    ): Single<NewsFeedResponse>

    companion object Factory {
        fun create(): NewsFeedService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.vk.com/method/")
                .build()

            return retrofit.create(NewsFeedService::class.java)
        }
    }
}