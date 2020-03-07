package com.arsoft.newsfeed.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.app.prefs
import com.arsoft.newsfeed.navigation.screens.Screens
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(){

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private var accessToken = ""

    private val navigator = object: SupportAppNavigator(this, R.id.main_container) {
        override fun applyCommands(commands: Array<Command>) {
            try {
                supportFragmentManager.executePendingTransactions()
                super.applyCommands(commands)

            } catch (error: IllegalStateException) {
                error.printStackTrace()
                Handler().postDelayed({
                    super.applyCommands(commands)
                }, 100)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(this)
        accessToken = prefs.accessToken
        if (savedInstanceState == null) {
            if (accessToken != "0") {
                navigator.applyCommands(arrayOf(Replace(Screens.NewsFeedScreen(accessToken = accessToken))))
            } else {
                navigator.applyCommands(arrayOf(Replace(Screens.LoginScreen())))
            }
        }
    }

//    override fun onBackPressed() {
//        navigator.applyCommands(arrayOf(Back()))
//    }



    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
