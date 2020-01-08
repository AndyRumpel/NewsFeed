package com.arsoft.newsfeed.data

import com.arsoft.newsfeed.data.login.repository.LoginRepository
import com.arsoft.newsfeed.data.login.request.LoginService
import com.arsoft.newsfeed.data.newsfeed.repository.NewsFeedRepository
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedService

class DataProvider {
    companion object {
        fun provideAccessToken(): LoginRepository {
            return LoginRepository(
                LoginService.create()
            )
        }

        fun provideNewsFeed(): NewsFeedRepository{
            return NewsFeedRepository(
                NewsFeedService.create()
            )
        }
    }
}