package com.arsoft.newsfeed.dagger.module

import android.content.Context
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.login.request.LoginService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class VkLoginModule {

    private val cacheSize = (5 * 1024 * 1024).toLong() // 5 MB
    private val cache = Cache(NewsFeedApplication.INSTANCE.cacheDir, cacheSize)

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .protocols(mutableListOf(Protocol.HTTP_1_1))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(60, TimeUnit.SECONDS)
            .cache(cache)
            .addNetworkInterceptor(provideCacheInterceptor())
            .addInterceptor(provideOfflineCacheInterceptor())
            .build()

    @Provides
    @Singleton
    @Named("VK_OAUTH")
    fun provideVkRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("http://oauth.vk.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideLoginService(@Named("VK_OAUTH")retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    private fun provideCacheInterceptor(): Interceptor{
        return Interceptor { chain ->
            val request = chain.request()
            val cacheHeaderValue = if (!NewsFeedApplication.INSTANCE.hasConnection()){
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 1
            } else {
                "public, max-age=" + 5
            }
            val response = chain.proceed(request)
            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheHeaderValue)
                .build()
        }
    }

    private fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            val cacheHeaderValue = if (!NewsFeedApplication.INSTANCE.hasConnection()){
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 1
            } else {
                "public, max-age=" + 5
            }
            request = request.newBuilder().header("Cache-Control", cacheHeaderValue).build()
            chain.proceed(request)
        }
    }
}