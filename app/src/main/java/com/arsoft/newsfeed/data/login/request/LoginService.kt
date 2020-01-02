package com.arsoft.newsfeed.data.login.request

import android.text.LoginFilter
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface LoginService {

    @GET(value = "token")
    fun login(
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: Long,
        @Query("client_secret") clientSecret: String,
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("scope") scope: String
    ): Single<LoginResponse>

    companion object Factory {
        fun create(): LoginService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://oauth.vk.com/")
                .build()

            return retrofit.create(LoginService::class.java)
        }
    }

}