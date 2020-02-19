package com.arsoft.newsfeed.mvp.comments

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.data.DataProvider
import kotlinx.coroutines.*

@InjectViewState
class CommentsPresenter: MvpPresenter<CommentsView>() {
    private val commentsRepository = DataProvider.provideComments()
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

}