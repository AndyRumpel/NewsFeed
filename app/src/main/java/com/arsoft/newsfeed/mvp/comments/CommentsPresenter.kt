package com.arsoft.newsfeed.mvp.comments

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.data.DataProvider
import kotlinx.coroutines.*

@InjectViewState
class CommentsPresenter: MvpPresenter<CommentsView>() {
    private val commentsRepository = DataProvider.provideComments()
    private val likesRepository = DataProvider.provideLikes()
    private var commentsJob: Job? = null

    fun loadCommetns(accessToken: String, ownerId: Long, postId: Long) {
        viewState.showLoading()
        commentsJob = GlobalScope.launch {
            val commentsList = commentsRepository.loadComments(accessToken = accessToken, ownerId = ownerId, postId = postId)
            withContext(Dispatchers.Main) {
                if (commentsList.isNotEmpty()) {
                    viewState.hideLoading()
                    viewState.loadCommentsList(commentsList = commentsList)
                    viewState.showCommentsList()
                } else {
                    viewState.hideLoading()
                    viewState.showEmptyCommentsList()
                }
            }
        }

    }

    fun addLike(type: String, ownerId: Long, itemId: Long, viewItemId: Long, accessToken: String) {
        commentsJob = GlobalScope.launch {
            val likes = likesRepository.addLike(type = type, ownerId = ownerId, itemId = itemId, accessToken = accessToken)
            withContext(Dispatchers.Main){
                Log.e("LIKE", "$likes")
                if (type == "post") {
                    viewState.updatePostLikesCount(likes = likes)
                } else {
                    viewState.updateCommentLikesCount(likes = likes, viewItemId = viewItemId)
                }
            }
        }
    }

    fun deleteLike(type: String, ownerId: Long, itemId: Long, viewItemId: Long, accessToken: String) {
        commentsJob = GlobalScope.launch {
            val likes = likesRepository.deleteLike(type = type, ownerId = ownerId, itemId = itemId, accessToken = accessToken)
            withContext(Dispatchers.Main){
                if (type == "post") {
                    viewState.updatePostLikesCount(likes = likes)
                } else {
                    viewState.updateCommentLikesCount(likes = likes, viewItemId = viewItemId)
                }
            }
        }
    }

    override fun onDestroy() {
        commentsJob!!.cancel()
        super.onDestroy()
    }

}