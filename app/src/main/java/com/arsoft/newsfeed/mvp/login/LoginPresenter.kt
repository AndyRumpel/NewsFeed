package com.arsoft.newsfeed.mvp.login

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.data.DataProvider
import com.arsoft.newsfeed.ui.screens.Screens
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router

@InjectViewState
class LoginPresenter(private val router: Router): MvpPresenter<LoginView>() {

    private val CLIENT_ID: Long = 2274003
    private val CLIENT_SECRET = "hHbZxrka2uZ6jB1inYsH"
    private val GRANT_TYPE = "password"
    private val SCOPE = "wall,friends"

    private val loginRepository = DataProvider.provideAccessToken()

    @SuppressLint("CheckResult")
    fun login(username: String, password: String) {
        viewState.showLoading()
        loginRepository.login(
            grantType = GRANT_TYPE,
            clientId = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            username = username,
            password = password,
            scope = SCOPE)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

            .subscribe({
                result ->
                viewState.hideLoading()
                successLoggingIn(accessToken = result.access_token)
            }, {
                error ->
                viewState.showError(error.message.toString())
                viewState.hideLoading()
            })
    }

    fun test(string: String){
        viewState.showError(string)
    }

    private fun successLoggingIn(accessToken: String) {
        viewState.saveAccessToken(accessToken = accessToken)
        router.newRootChain(Screens.NewsFeedScreen(accessToken = accessToken))
    }
}