package com.arsoft.newsfeed.data.likes.request

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface LikesService {

    @GET(value = "likes.add")
    fun addLike(
        @Query(value = "type") type: String,
        @Query(value = "owner_id") ownerId: Long,
        @Query(value = "item_id") itemId: Long,
        @Query(value = "access_token") accessToken: String,
        @Query(value = "v") version: String
    ): Deferred<LikesResponse>

    @GET(value = "likes.delete")
    fun deleteLike(
        @Query(value = "type") type: String,
        @Query(value = "owner_id") ownerId: Long,
        @Query(value = "item_id") itemId: Long,
        @Query(value = "access_token") accessToken: String,
        @Query(value = "v") version: String
    ): Deferred<LikesResponse>

    companion object Factory {
        fun create(): LikesService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.vk.com/method/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(LikesService::class.java)
        }
    }
}