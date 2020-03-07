package com.arsoft.newsfeed.dagger.module

import com.arsoft.newsfeed.data.comments.request.CommentsService
import com.arsoft.newsfeed.data.likes.request.LikesService
import com.arsoft.newsfeed.data.newsfeed.request.*
import com.arsoft.newsfeed.data.video.request.VideoService
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class VkRequestModule {

    @Provides
    @Singleton
    @Named("VK_API")
    fun provideVkRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://api.vk.com/method/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideNewsFeedService(@Named("VK_API")retrofit: Retrofit): NewsFeedService =
        retrofit.create(NewsFeedService::class.java)

    @Provides
    @Singleton
    fun provideCommentsService(@Named("VK_API")retrofit: Retrofit): CommentsService =
        retrofit.create(CommentsService::class.java)

    @Provides
    @Singleton
    fun provideLikesService(@Named("VK_API")retrofit: Retrofit): LikesService =
        retrofit.create(LikesService::class.java)

    @Provides
    @Singleton
    fun provideVideoPlayerService(@Named("VK_API")retrofit: Retrofit): VideoService =
        retrofit.create(VideoService::class.java)
}

