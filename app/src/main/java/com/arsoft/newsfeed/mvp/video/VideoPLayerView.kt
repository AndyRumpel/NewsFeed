package com.arsoft.newsfeed.mvp.video

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface VideoPLayerView: MvpView {

    fun initializePlayer(videoURL: String)
    fun playVideo()
    fun chooseQuality(qualities: ArrayList<String>)
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)

}