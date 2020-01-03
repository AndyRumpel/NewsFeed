package com.arsoft.newsfeed.mvp.newsfeed

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedResponse

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface NewsFeedView: MvpView {
    fun loadNewsFeed(model: NewsFeedResponse)
    fun showLoading()
    fun hideLoading()
    fun showEmptyList()
    fun showError(message: String)
}