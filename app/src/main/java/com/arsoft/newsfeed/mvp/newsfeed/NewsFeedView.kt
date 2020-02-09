package com.arsoft.newsfeed.mvp.newsfeed

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.arsoft.newsfeed.data.likes.request.LikesResponse
import com.arsoft.newsfeed.data.models.FeedItemModel
import java.lang.NullPointerException

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface NewsFeedView: MvpView {
    fun loadNewsFeed(items: ArrayList<FeedItemModel>)
    fun refreshNewsFeed(items: ArrayList<FeedItemModel>)
    fun showLoading()
    fun hideLoading()
    fun showEmptyList()
    fun updateLikesCount(likes: LikesResponse, position: Int)
    fun showError(message: String)
}