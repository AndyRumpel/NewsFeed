package com.arsoft.newsfeed.dagger

import com.arsoft.newsfeed.dagger.module.NavigationModule
import com.arsoft.newsfeed.dagger.module.VkLoginModule
import com.arsoft.newsfeed.dagger.module.VkRequestModule
import com.arsoft.newsfeed.mvp.comments.CommentsPresenter
import com.arsoft.newsfeed.mvp.login.LoginPresenter
import com.arsoft.newsfeed.mvp.newsfeed.NewsFeedPresenter
import com.arsoft.newsfeed.mvp.video.VideoPlayerPresenter
import com.arsoft.newsfeed.ui.comments.CommentsFragment
import com.arsoft.newsfeed.ui.login.LoginFragment
import com.arsoft.newsfeed.ui.main.MainActivity
import com.arsoft.newsfeed.ui.newsfeed.NewsFeedFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class, VkRequestModule::class, VkLoginModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: NewsFeedFragment)
    fun inject(presenter: NewsFeedPresenter)
    fun inject(presenter: CommentsPresenter)
    fun inject(presenter: LoginPresenter)
    fun inject(presenter: VideoPlayerPresenter)
    fun inject(fragment: CommentsFragment)
}