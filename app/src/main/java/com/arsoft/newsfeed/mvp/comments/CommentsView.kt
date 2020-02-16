package com.arsoft.newsfeed.mvp.comments

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface CommentsView: MvpView {
    fun showCommentsList()
    fun showEmptyCommentsList()
    fun sendComment()
    fun showLoading()
    fun hideLoading()
}