package com.arsoft.newsfeed.dagger

import com.arsoft.newsfeed.adapters.NewsFeedRecyclerAdapter
import com.arsoft.newsfeed.dagger.module.NavigationModule
import com.arsoft.newsfeed.ui.login.LoginFragment
import com.arsoft.newsfeed.ui.main.MainActivity
import com.arsoft.newsfeed.ui.newsfeed.NewsFeedFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: NewsFeedFragment)
    fun inject(viewHolder: NewsFeedRecyclerAdapter.NewsFeedViewHolder)
}