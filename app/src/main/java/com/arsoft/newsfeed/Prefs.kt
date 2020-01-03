package com.arsoft.newsfeed

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val PREFS_FILENAME = "com.arsoft.prefs"
    private val ACCESS_TOKEN = "access_token"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var accessToken: String
        get() = prefs.getString(ACCESS_TOKEN, "0")!!
        set(value) = prefs.edit().putString(ACCESS_TOKEN, value).apply()
}