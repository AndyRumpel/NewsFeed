package com.arsoft.newsfeed.mvp.comments

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.arsoft.newsfeed.data.likes.request.LikesResponse
import com.arsoft.newsfeed.data.models.CommentModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface CommentsView: MvpView {
    fun loadCommentsList(commentsList: ArrayList<CommentModel>)
    fun showCommentsList()
    fun showEmptyCommentsList()
    fun sendComment()
    fun showLoading()
    fun hideLoading()
    fun updatePostLikesCount(likes: LikesResponse)
    fun updateCommentLikesCount(likes: LikesResponse, viewItemId: Long)
}