package com.arsoft.newsfeed.data

import com.arsoft.newsfeed.data.comments.repository.CommentsRepository
import com.arsoft.newsfeed.data.comments.request.CommentsService
import com.arsoft.newsfeed.data.likes.repository.LikesRepository
import com.arsoft.newsfeed.data.likes.request.LikesService
import com.arsoft.newsfeed.data.login.repository.LoginRepository
import com.arsoft.newsfeed.data.login.request.LoginService
import com.arsoft.newsfeed.data.newsfeed.repository.NewsFeedRepository
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedService
import com.arsoft.newsfeed.data.video.repository.VideoRepository
import com.arsoft.newsfeed.data.video.request.VideoService

class DataProvider {
    companion object {
        fun provideAccessToken(apiService: LoginService): LoginRepository {
            return LoginRepository(
                apiService
            )
        }

        fun provideNewsFeed(apiService: NewsFeedService): NewsFeedRepository{
            return NewsFeedRepository(
                apiService
            )
        }

        fun provideVideo(apiService: VideoService): VideoRepository{
            return VideoRepository(
                apiService
            )
        }

        fun provideLikes(apiService: LikesService): LikesRepository {
            return LikesRepository(
                apiService
            )
        }

        fun provideComments(apiService: CommentsService): CommentsRepository {
            return CommentsRepository(
                apiService
            )
        }

    }
}