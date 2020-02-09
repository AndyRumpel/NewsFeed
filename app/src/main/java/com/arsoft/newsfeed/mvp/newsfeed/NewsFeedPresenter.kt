package com.arsoft.newsfeed.mvp.newsfeed

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.DataProvider
import com.arsoft.newsfeed.data.likes.repository.LikesRepository
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedService
import kotlinx.coroutines.*
import javax.inject.Inject

@InjectViewState
class NewsFeedPresenter: MvpPresenter<NewsFeedView>() {

    @Inject
    lateinit var newsFeedApiService: NewsFeedService

    private val likesRepository = DataProvider.provideLikes()

    init {
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(this)
    }

    private val newsFeedRepository = DataProvider.provideNewsFeed(newsFeedApiService)
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
                if (newsFeedList.isNotEmpty()) {
                    viewState.loadNewsFeed(items = newsFeedList)
                }
            }
        }
    }

    fun addLike(ownerId: Long, itemId: Long, accessToken: String, position: Int) {
        newsFeedJob = GlobalScope.launch {
            val likes = likesRepository.addLike(ownerId = ownerId, itemId = itemId, accessToken = accessToken)
            withContext(Dispatchers.Main){
                viewState.updateLikesCount(likes = likes, position =  position)
            }
        }
    }

    fun deleteLike(ownerId: Long, itemId: Long, accessToken: String, position: Int) {
        newsFeedJob = GlobalScope.launch {
            val likes = likesRepository.deleteLike(ownerId = ownerId, itemId = itemId, accessToken = accessToken)
            withContext(Dispatchers.Main) {
                viewState.updateLikesCount(likes = likes, position = position)
            }
        }
    }

    override fun onDestroy() {
        newsFeedJob?.cancel()
        super.onDestroy()
    }
}