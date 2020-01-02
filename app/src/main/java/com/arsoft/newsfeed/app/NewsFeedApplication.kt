package com.arsoft.newsfeed.app

import android.app.Application
import com.arsoft.newsfeed.dagger.AppComponent
import com.arsoft.newsfeed.dagger.DaggerAppComponent

class NewsFeedApplication: Application() {

    companion object {
         lateinit var INSTANCE: NewsFeedApplication
    }

    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    fun getAppComponent(): AppComponent? {
        if (appComponent == null)
            appComponent = DaggerAppComponent.builder().build()
        return appComponent
    }

}