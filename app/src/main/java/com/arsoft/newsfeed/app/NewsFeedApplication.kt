package com.arsoft.newsfeed.app

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.arsoft.newsfeed.helpers.Prefs
import com.arsoft.newsfeed.dagger.AppComponent
import com.arsoft.newsfeed.dagger.DaggerAppComponent
import com.arsoft.newsfeed.dagger.module.NavigationModule
import com.arsoft.newsfeed.dagger.module.VkLoginModule
import com.arsoft.newsfeed.dagger.module.VkRequestModule

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
            appComponent = DaggerAppComponent.builder()
                .navigationModule(NavigationModule())
                .vkRequestModule(VkRequestModule())
                .vkLoginModule(VkLoginModule())
                .build()
        return appComponent
    }

    fun hasConnection(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

}