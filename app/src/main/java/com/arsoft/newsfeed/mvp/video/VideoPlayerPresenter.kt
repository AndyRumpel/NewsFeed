package com.arsoft.newsfeed.mvp.video

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.DataProvider
import com.arsoft.newsfeed.data.video.request.VideoService
import kotlinx.coroutines.*
import javax.inject.Inject

@InjectViewState
class VideoPlayerPresenter: MvpPresenter<VideoPLayerView>() {

    @Inject
    lateinit var videoService: VideoService

    init {
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(presenter = this)
    }

    private val dataProvider = DataProvider.provideVideo(apiService = videoService)
    private val videoLoadingJob: Job? = null


    fun loadVideo(ownerID: Long, videoID: String, accessToken: String){
        viewState.showLoading()

        GlobalScope.launch {
            val videoURL = dataProvider.getVideoLink(ownerID = ownerID, videoID = videoID, accessToken = accessToken, platform = "VK")
            withContext(Dispatchers.Main) {
                try {
                    viewState.hideLoading()
                    viewState.initializePlayer(videoURL = videoURL)
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