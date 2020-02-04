package com.arsoft.newsfeed.data.newsfeed.request

import kotlinx.coroutines.Deferred
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
}