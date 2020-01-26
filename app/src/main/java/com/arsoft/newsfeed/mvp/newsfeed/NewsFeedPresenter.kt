package com.arsoft.newsfeed.mvp.newsfeed

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.data.DataProvider
import com.arsoft.newsfeed.data.models.FeedItemModel
import kotlinx.coroutines.*

@InjectViewState
class NewsFeedPresenter: MvpPresenter<NewsFeedView>() {

    private val newsFeedRepository = DataProvider.provideNewsFeed()
    private var newsFeedJob: Job? = null

    fun loadNewsFeed(accessToken: String){
        viewState.showLoading()
        newsFeedJob = GlobalScope.launch {
            val newsFeedList: ArrayList<FeedItemModel> = newsFeedRepository.getNewsFeed(accessToken = accessToken)
            withContext(Dispatchers.Main) {
                if (newsFeedList.isNotEmpty()) {
                    viewState.hideLoading()
                    viewState.loadNewsFeed(items = newsFeedList)
                }
            }
        }
    }

    override fun onDestroy() {
        newsFeedJob?.cancel()
        super.onDestroy()
    }
}