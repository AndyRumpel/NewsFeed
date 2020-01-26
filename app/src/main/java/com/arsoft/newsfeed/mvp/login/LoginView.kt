package com.arsoft.newsfeed.mvp.login

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface LoginView: MvpView {
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
}