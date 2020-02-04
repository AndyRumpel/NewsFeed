package com.arsoft.newsfeed.data

import com.arsoft.newsfeed.data.login.repository.LoginRepository
import com.arsoft.newsfeed.data.login.request.LoginService
import com.arsoft.newsfeed.data.newsfeed.repository.NewsFeedRepository
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedService
import com.arsoft.newsfeed.data.video.repository.VideoPlayerRepository
import com.arsoft.newsfeed.data.video.request.VideoPlayerService

class DataProvider {
    companion object {
        fun provideAccessToken(): LoginRepository {
            return LoginRepository(
                LoginService.create()
            )
        }

        fun provideNewsFeed(apiService: NewsFeedService): NewsFeedRepository{
            return NewsFeedRepository(
                apiService
            )
        }

        fun provideVideo(): VideoPlayerRepository{
            return VideoPlayerRepository(
                VideoPlayerService.create()
            )
        }

    }
}