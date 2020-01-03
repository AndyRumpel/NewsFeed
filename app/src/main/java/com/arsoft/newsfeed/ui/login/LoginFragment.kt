package com.arsoft.newsfeed.ui.login


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.app.NewsFeedApplication.Companion.prefs
import com.arsoft.newsfeed.mvp.login.LoginPresenter
import com.arsoft.newsfeed.mvp.login.LoginView
import com.arsoft.newsfeed.ui.screens.Screens
import kotlinx.android.synthetic.main.fragment_login.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LoginFragment: MvpAppCompatFragment(), LoginView {

    private val APP_PREFERENCES_ACCESS_TOKEN = "access_token"

    //MARK -
    companion object {
        fun getNewInstance() = LoginFragment().apply {
            arguments = Bundle().apply {
                //TODO put arguments here
            }
        }
    }
    @Inject
    lateinit var router: Router

    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    internal fun providePresenter(): LoginPresenter {
        return LoginPresenter(router)

    }

    init {
        NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(this)
        presenter = LoginPresenter(router)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener{
            if (usernameInput.text.toString() != "" && passwordInput.text.toString() != "") {
                presenter.login(username = usernameInput.text.toString(), password = passwordInput.text.toString())
            } else {
                Toast.makeText(context, "Введите правильные данные", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun showLoading() {
        usernameInput.visibility = View.INVISIBLE
        passwordInput.visibility = View.INVISIBLE
        loginButton.visibility = View.INVISIBLE
        loading_cpv.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        usernameInput.visibility = View.VISIBLE
        passwordInput.visibility = View.VISIBLE
        loginButton.visibility = View.VISIBLE
        loading_cpv.visibility = View.INVISIBLE
    }

    override fun saveAccessToken(accessToken: String) {
        prefs!!.accessToken = accessToken
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Log.e("ERROR", message)
    }

}
