package com.arsoft.newsfeed.mvp.newsfeed

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.data.DataProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class NewsFeedPresenter: MvpPresenter<NewsFeedView>() {

    private val ITEMS_COUNT = 50
    private val VERSION = "5.103"
    private val FILTERS = "post"
    private val newsFeedRepository = DataProvider.provideNewsFeed()

    fun loadNewsFeed(accessToken: String){
        viewState.showLoading()
        newsFeedRepository.getNewsFeed(
            count = ITEMS_COUNT,
            accessToken = accessToken,
            version = VERSION,
            filters = FILTERS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                result ->
                viewState.hideLoading()
                viewState.loadNewsFeed(result)
            },{
                viewState.hideLoading()
                viewState.showEmptyList()
            })
    }

}