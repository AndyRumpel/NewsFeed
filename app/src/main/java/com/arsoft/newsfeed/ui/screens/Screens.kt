package com.arsoft.newsfeed.ui.screens

import androidx.fragment.app.Fragment
import com.arsoft.newsfeed.ui.login.LoginFragment
import com.arsoft.newsfeed.ui.newsfeed.NewsFeedFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class LoginScreen: SupportAppScreen() {
        override fun getFragment(): Fragment {
            return LoginFragment.getNewInstance()
        }
    }

    class NewsFeedScreen(private val accessToken: String): SupportAppScreen() {
        override fun getFragment(): Fragment {
            return NewsFeedFragment.getNewInstance(accessToken)
        }
    }
}