package com.arsoft.newsfeed.data.login.repository

import com.arsoft.newsfeed.data.login.request.LoginResponse
import com.arsoft.newsfeed.data.login.request.LoginService
import io.reactivex.Single

class LoginRepository(val apiService: LoginService) {
    fun login(
            grantType: String,
            clientId: Long,
            clientSecret: String,
            username: String,
            password: String,
            scope: String): Single<LoginResponse> {
        return apiService.login(
            grantType = grantType,
            clientId = clientId,
            clientSecret = clientSecret,
            username = username,
            password = password,
            scope = scope)
    }
}