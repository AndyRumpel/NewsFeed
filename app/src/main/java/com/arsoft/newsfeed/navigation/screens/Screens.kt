package com.arsoft.newsfeed.navigation.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.ui.comments.CommentsFragment
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
        private val ownerID: Long,
        private val videoID: String
    ): SupportAppScreen() {
        override fun getFragment(): Fragment {
            return VideoPlayerFragment.getNewInstance(ownerID = ownerID, videoID = videoID)
        }
    }

    class CommentsScreen(
        private val model: FeedItemModel
    ): SupportAppScreen() {
        override fun getFragment(): Fragment {
            return CommentsFragment.getNewInstance(model = model)
        }
    }

    class ExternalPlayerActivity(private val videoURL: String): SupportAppScreen(){
        override fun getActivityIntent(context: Context?): Intent {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoURL))
            return Intent.createChooser(intent, "Choose App")
        }
    }
}