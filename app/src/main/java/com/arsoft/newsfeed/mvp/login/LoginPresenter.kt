package com.arsoft.newsfeed.mvp.login

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.data.login.repository.DataProvider
import com.arsoft.newsfeed.ui.screens.Screens
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router

@InjectViewState
class LoginPresenter(private val router: Router): MvpPresenter<LoginView>() {
    private val loginRepository = DataProvider.provideAccessToken()

    fun login(username: String, password: String) {
        viewState.startLoading()
        loginRepository.login(
            grantType = "password",
            clientId = 2274003,
            clientSecret = "hHbZxrka2uZ6jB1inYsH",
            username = username,
            password = password,
            scope = "wall,friends")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

            .subscribe({
                result ->
                viewState.stopLoading()
                viewState.saveAccessToken(accessToken = result.access_token)
                successLoggingIn(accessToken = result.access_token)
            }, {
                error ->
                viewState.stopLoading()
            })
    }

    fun test(string: String){
        viewState.showError(string)
    }

    private fun successLoggingIn(accessToken: String) {
        router.newRootChain(Screens.NewsFeedScreen(accessToken = accessToken))
    }


}