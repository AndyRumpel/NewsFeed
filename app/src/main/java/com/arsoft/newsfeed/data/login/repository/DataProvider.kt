package com.arsoft.newsfeed.data.login.repository

import com.arsoft.newsfeed.data.login.request.LoginService

class DataProvider {
    companion object {
        fun provideAccessToken(): LoginRepository {
            return LoginRepository(
                LoginService.create()
            )
        }
    }
}