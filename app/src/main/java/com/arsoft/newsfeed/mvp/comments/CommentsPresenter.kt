package com.arsoft.newsfeed.mvp.comments

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.DataProvider
import com.arsoft.newsfeed.data.comments.request.CommentsService
import com.arsoft.newsfeed.data.likes.request.LikesService
import com.arsoft.newsfeed.data.video.request.VideoService
import kotlinx.coroutines.*
import javax.inject.Inject

@InjectViewState
class CommentsPresenter: MvpPresenter<CommentsView>() {

    @Inject
    lateinit var commentsService: CommentsService

    @Inject
    lateinit var likesService: LikesService

    @Inject
    lateinit var videoService: VideoService

    init {
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(this)
    }

    private val commentsRepository = DataProvider.provideComments(commentsService)
    private val likesRepository = DataProvider.provideLikes(likesService)
    private val videoRepository = DataProvider.provideVideo(videoService)
    private var commentsJob: Job? = null

    fun loadComments(accessToken: String, ownerId: Long, postId: Long) {
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
                viewState.updateCommentLikesCount(likes = likes, viewItemId = viewItemId)
            }
        }
    }
    fun addLike(type: String, ownerId: Long, itemId: Long, accessToken: String) {
        commentsJob = GlobalScope.launch {
            val likes = likesRepository.addLike(type = type, ownerId = ownerId, itemId = itemId, accessToken = accessToken)
            withContext(Dispatchers.Main){
                viewState.updatePostLikesCount(likes = likes)
            }
        }
    }

    fun deleteLike(type: String, ownerId: Long, itemId: Long, viewItemId: Long, accessToken: String) {
        commentsJob = GlobalScope.launch {
            val likes = likesRepository.deleteLike(type = type, ownerId = ownerId, itemId = itemId, accessToken = accessToken)
            withContext(Dispatchers.Main){
                viewState.updateCommentLikesCount(likes = likes, viewItemId = viewItemId)
            }
        }
    }
    fun deleteLike(type: String, ownerId: Long, itemId: Long, accessToken: String) {
        commentsJob = GlobalScope.launch {
            val likes = likesRepository.deleteLike(type = type, ownerId = ownerId, itemId = itemId, accessToken = accessToken)
            withContext(Dispatchers.Main){
                viewState.updatePostLikesCount(likes = likes)
            }
        }
    }

    fun createComment(ownerId: Long, postId: Long, accessToken: String, message: String) {
        commentsJob = GlobalScope.launch {
            val createdComment = commentsRepository.createComment(ownerId = ownerId, postId = postId, accessToken = accessToken, message = message)
            withContext(Dispatchers.Main){
                createdComment.let {
                    viewState.updateComments()
                }
            }
        }
    }

    fun replyComment(ownerId: Long, postId: Long, accessToken: String, replyToComment: Long, message: String) {
        commentsJob = GlobalScope.launch {
            val repliedComment = commentsRepository.replyComment(ownerId = ownerId, postId = postId, accessToken = accessToken, replyToComment = replyToComment, message = message)
            withContext(Dispatchers.Main){
                repliedComment.let {
                    viewState.updateComments()
                }
            }
        }
    }

    fun loadExternalVideo(ownerID: Long, videoID: String, accessToken: String){
        commentsJob = GlobalScope.launch {
            val videoURL = videoRepository.getVideoLink(ownerID = ownerID, videoID = videoID, accessToken = accessToken, platform = "YouTube")
            withContext(Dispatchers.Main) {
                viewState.openExternalPlayer(videoURL = videoURL)
            }
        }
    }


    override fun onDestroy() {
        commentsJob!!.cancel()
        super.onDestroy()
    }

}