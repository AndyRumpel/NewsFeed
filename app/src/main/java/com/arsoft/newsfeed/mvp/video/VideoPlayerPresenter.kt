package com.arsoft.newsfeed.mvp.video

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.data.DataProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@InjectViewState
class VideoPlayerPresenter: MvpPresenter<VideoPLayerView>() {

    private val VERSION = "5.103"
    private val dataProvider = DataProvider.provideVideo()


    fun loadPLayer(ownerID: String, videoID: String, accessToken: String){
        viewState.showLoading()
        dataProvider.getVideo(
            ownerID = ownerID,
            videoID = videoID,
            accessToken = accessToken,
            version = VERSION)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                viewState.hideLoading()
                if (result.response.items.first().files.mp4_720 != null) {
                    viewState.initializePlayer(result.response.items.first().files.mp4_720)
                    viewState.playVideo()
                }
            }, {error ->
                viewState.hideLoading()
                viewState.showError(error.message!!)
            })
    }

}