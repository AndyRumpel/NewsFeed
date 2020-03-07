package com.arsoft.newsfeed.mvp.newsfeed

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.arsoft.newsfeed.data.likes.request.LikesResponse
import com.arsoft.newsfeed.data.models.FeedItemModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface NewsFeedView: MvpView {
    fun loadNewsFeed(items: ArrayList<FeedItemModel>)
    fun loadMoreNewsFeed(items: ArrayList<FeedItemModel>)
    fun refreshNewsFeed(items: ArrayList<FeedItemModel>)
    fun showLoading()
    fun hideLoading()
    fun updateLikesCount(likes: LikesResponse, viewItemId: Long)
    fun openExternalPlayer(videoURL: String)
    fun showError(message: String)
}