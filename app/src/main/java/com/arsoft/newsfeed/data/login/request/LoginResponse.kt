package com.arsoft.newsfeed.data.login.request

data class LoginResponse(
    val access_token: String,
    val expires_in: Int,
    val user_id: Long
)