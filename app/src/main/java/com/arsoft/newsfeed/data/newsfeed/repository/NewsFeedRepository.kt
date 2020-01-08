package com.arsoft.newsfeed.data.newsfeed.repository

import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedResponse
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedService
import io.reactivex.Single

class NewsFeedRepository(val apiService: NewsFeedService) {

    fun getNewsFeed(count: Int, accessToken: String, version: String, filters: String): Single<NewsFeedResponse>{
        return apiService.getNewsFeed(count = count, accessToken = accessToken, version = version, filters = filters)
    }
}