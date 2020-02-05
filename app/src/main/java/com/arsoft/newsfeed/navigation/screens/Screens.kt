package com.arsoft.newsfeed.navigation.screens

import androidx.fragment.app.Fragment
import com.arsoft.newsfeed.ui.login.LoginFragment
import com.arsoft.newsfeed.ui.newsfeed.NewsFeedFragment
import com.arsoft.newsfeed.ui.photo.ViewPhotoFragment
import com.arsoft.newsfeed.ui.video.VideoPlayerFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class LoginScreen: SupportAppScreen() {
        override fun getFragment(): Fragment {
            return LoginFragment.getNewInstance()
        }
    }

    class NewsFeedScreen(private val accessToken: String): SupportAppScreen() {
        override fun getFragment(): Fragment {
            return NewsFeedFragment.getNewInstance(accessToken = accessToken)
        }
    }

    class ViewPhotoScreen(private val photoURLs: ArrayList<String?>, private val position: Int): SupportAppScreen() {
        override fun getFragment(): Fragment {
            return ViewPhotoFragment.getNewInstance(photoURLs = photoURLs, position =  position)
        }
    }

    class VideoPlayerScreen(
        private val videoID: String,
        private val videoOwnerID: String
    ): SupportAppScreen() {
        override fun getFragment(): Fragment {
            return VideoPlayerFragment.getNewInstance(videoID = videoID, videoOwnerID = videoOwnerID)
        }
    }
}