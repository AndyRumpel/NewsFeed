package com.arsoft.newsfeed.ui.main

import android.content.Context
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.ui.screens.Screens
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = object: SupportAppNavigator(this, R.id.main_container) {
        override fun applyCommands(commands: Array<Command>) {
            super.applyCommands(commands)
            supportFragmentManager.executePendingTransactions()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(this)

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val accessToken = sharedPref.getString("accessToken", null)
        if (accessToken == null) {
            navigator.applyCommands(arrayOf(Replace(Screens.LoginScreen())))
        } else {
            navigator.applyCommands(arrayOf(Replace(Screens.NewsFeedScreen(accessToken))))
        }
    }

    override fun onResumeFragments() {
        navigatorHolder.setNavigator(navigator)
        super.onResumeFragments()
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
