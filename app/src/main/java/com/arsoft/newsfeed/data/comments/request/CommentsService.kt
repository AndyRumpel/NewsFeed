package com.arsoft.newsfeed.data.comments.request

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CommentsService {

    @GET(value = "wall.getComments")
    fun getComments(
        @Query(value = "owner_id") ownerId: Long,
        @Query(value = "post_id") post_id: Long,
        @Query(value = "access_token") accessToken: String,
        @Query(value = "v") version: String,
        @Query(value = "extended") extended: Int,
        @Query(value = "need_likes") needLikes: Int,
        @Query(value = "thread_items_count") threadItemsCount: Int,
        @Query(value = "count") count: Int
    ): Deferred<CommentsResponse>

    companion object Factory {
        fun create(): CommentsService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.vk.com/method/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(CommentsService::class.java)
        }
    }
}