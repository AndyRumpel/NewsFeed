package com.arsoft.newsfeed.mvp.login

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.app.NewsFeedApplication.Companion.prefs
import com.arsoft.newsfeed.data.DataProvider
import com.arsoft.newsfeed.data.login.request.LoginService
import com.arsoft.newsfeed.navigation.screens.Screens
import kotlinx.coroutines.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class LoginPresenter(private val router: Router): MvpPresenter<LoginView>() {

    @Inject
    lateinit var loginService: LoginService

    init {
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(presenter = this)
    }

    private val loginRepository = DataProvider.provideAccessToken(apiService = loginService)
    private var loginJob: Job? = null

    fun login(username: String, password: String) {
        viewState.showLoading()
        loginJob = GlobalScope.launch {
            val result = loginRepository.login(username = username, password = password)
            withContext(Dispatchers.Main) {
                viewState.hideLoading()
                prefs!!.accessToken = result.access_token
                router.newRootChain(Screens.NewsFeedScreen(accessToken = result.access_token))
            }
        }
    }

    override fun onDestroy() {
        loginJob?.cancel()
        super.onDestroy()
    }
}