package com.arsoft.newsfeed.data.login.repository

import com.arsoft.newsfeed.data.login.request.LoginResponse
import com.arsoft.newsfeed.data.login.request.LoginService

class LoginRepository(private val apiService: LoginService) {

    private val CLIENT_ID: Long = 2274003
    private val CLIENT_SECRET = "hHbZxrka2uZ6jB1inYsH"
    private val GRANT_TYPE = "password"
    private val SCOPE = "wall,friends"

    suspend fun login(
            username: String,
            password: String): LoginResponse {
        return apiService.login(
            grantType = GRANT_TYPE,
            clientId = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            username = username,
            password = password,
            scope = SCOPE).await()
    }
}