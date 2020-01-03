package com.arsoft.newsfeed.app

import android.app.Application
import com.arsoft.newsfeed.Prefs
import com.arsoft.newsfeed.dagger.AppComponent
import com.arsoft.newsfeed.dagger.DaggerAppComponent

val prefs: Prefs by lazy {
    NewsFeedApplication.prefs!!
}

class NewsFeedApplication: Application() {

    companion object {
        lateinit var INSTANCE: NewsFeedApplication
        var prefs: Prefs? = null
    }

    private var appComponent: AppComponent? = null

    override fun onCreate() {
        prefs = Prefs(applicationContext)
        super.onCreate()
        INSTANCE = this
    }

    fun getAppComponent(): AppComponent? {
        if (appComponent == null)
            appComponent = DaggerAppComponent.builder().build()
        return appComponent
    }

}