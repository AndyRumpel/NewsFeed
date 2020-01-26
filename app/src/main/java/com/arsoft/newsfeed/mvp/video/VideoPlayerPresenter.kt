package com.arsoft.newsfeed.mvp.video

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.data.DataProvider
import kotlinx.coroutines.*

@InjectViewState
class VideoPlayerPresenter: MvpPresenter<VideoPLayerView>() {


    private val dataProvider = DataProvider.provideVideo()
    private val videoLoadingJob: Job? = null


    fun loadPLayer(ownerID: String, videoID: String, accessToken: String){
        viewState.showLoading()

        GlobalScope.launch {
            val videoURL = dataProvider.getVideoLink(ownerID = ownerID, videoID = videoID, accessToken = accessToken)
            withContext(Dispatchers.Main) {
                try {
                    viewState.hideLoading()
                    viewState.initializePlayer(videoURL)
                    viewState.playVideo()
                } catch (e: Throwable) {
                    viewState.showError(e.message.toString())
                }
            }
        }
    }

    override fun onDestroy() {
        videoLoadingJob?.cancel()
        super.onDestroy()
    }
}