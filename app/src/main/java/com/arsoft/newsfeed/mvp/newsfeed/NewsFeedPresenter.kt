package com.arsoft.newsfeed.mvp.newsfeed

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.DataProvider
import com.arsoft.newsfeed.data.likes.request.LikesService
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedService
import com.arsoft.newsfeed.data.video.request.VideoService
import kotlinx.coroutines.*
import javax.inject.Inject

@InjectViewState
class NewsFeedPresenter: MvpPresenter<NewsFeedView>() {

    @Inject
    lateinit var newsFeedApiService: NewsFeedService

    @Inject
    lateinit var likesService: LikesService

    @Inject
    lateinit var videoService: VideoService

    init {
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(presenter = this)
    }

    private val newsFeedRepository = DataProvider.provideNewsFeed(apiService = newsFeedApiService)
    private val likesRepository = DataProvider.provideLikes(apiService = likesService)
    private val videoRepository = DataProvider.provideVideo(apiService = videoService)
    private var newsFeedJob: Job? = null



    fun loadNewsFeed(accessToken: String){
        viewState.showLoading()
        newsFeedJob = GlobalScope.launch {
            val newsFeedList = newsFeedRepository.getNewsFeed(accessToken = accessToken)
            withContext(Dispatchers.Main) {
                if (newsFeedList.isNotEmpty()) {
                    viewState.hideLoading()
                    viewState.loadNewsFeed(items = newsFeedList)
                }
            }
        }
    }

    fun refreshNewsFeed(accessToken: String) {
        newsFeedJob = GlobalScope.launch {
            val newsFeedList = newsFeedRepository.getNewsFeed(accessToken = accessToken)
            withContext(Dispatchers.Main) {
                viewState.refreshNewsFeed(items = newsFeedList)
            }
        }
    }

    fun loadMoreNewsFeed(accessToken: String, startFrom: String) {
        newsFeedJob = GlobalScope.launch {
            val newsFeedList = newsFeedRepository.loadMoreNewsFeed(accessToken = accessToken, startFrom = startFrom)
            withContext(Dispatchers.Main) {
                viewState.loadMoreNewsFeed(items = newsFeedList)
            }
        }
    }

    fun addLike(type: String, ownerId: Long, itemId: Long, accessToken: String, viewItemId: Long) {
        newsFeedJob = GlobalScope.launch {
            val likes = likesRepository.addLike(type = type, ownerId = ownerId, itemId = itemId, accessToken = accessToken)
            withContext(Dispatchers.Main){
                viewState.updateLikesCount(likes = likes, viewItemId =  viewItemId)
            }
        }
    }

    fun deleteLike(type: String, ownerId: Long, itemId: Long, accessToken: String, viewItemId: Long) {
        newsFeedJob = GlobalScope.launch {
            val likes = likesRepository.deleteLike(type = type, ownerId = ownerId, itemId = itemId, accessToken = accessToken)
            withContext(Dispatchers.Main) {
                viewState.updateLikesCount(likes = likes, viewItemId = viewItemId)
            }
        }
    }

    fun loadExternalVideo(ownerID: Long, videoID: String, accessToken: String){
        newsFeedJob = GlobalScope.launch {
            val videoURL = videoRepository.getVideoLink(ownerID = ownerID, videoID = videoID, accessToken = accessToken, platform = "YouTube")
            withContext(Dispatchers.Main) {
                viewState.openExternalPlayer(videoURL = videoURL)
            }
        }
    }



    override fun onDestroy() {
        newsFeedJob?.cancel()
        super.onDestroy()
    }
}