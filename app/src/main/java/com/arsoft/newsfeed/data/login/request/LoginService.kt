package com.arsoft.newsfeed.data.login.request

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {

    @GET(value = "token")
    fun login(
        @Query(value = "grant_type") grantType: String,
        @Query(value = "client_id") clientId: Long,
        @Query(value = "client_secret") clientSecret: String,
        @Query(value = "username") username: String,
        @Query(value = "password") password: String,
        @Query(value = "scope") scope: String
    ): Deferred<LoginResponse>

    companion object Factory {
        fun create(): LoginService {
            val okHttpClient = OkHttpClient().newBuilder()
                .protocols(mutableListOf(Protocol.HTTP_1_1))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://oauth.vk.com/")
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(LoginService::class.java)
        }
    }

}