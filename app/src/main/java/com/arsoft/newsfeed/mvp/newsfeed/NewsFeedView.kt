package com.arsoft.newsfeed.mvp.newsfeed

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.arsoft.newsfeed.data.models.FeedItemModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface NewsFeedView: MvpView {
    fun loadNewsFeed(items: ArrayList<FeedItemModel>)
    fun showLoading()
    fun hideLoading()
    fun showEmptyList()
    fun showError(message: String)
}