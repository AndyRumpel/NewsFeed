package com.arsoft.newsfeed.ui.login


import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.mvp.login.LoginPresenter
import com.arsoft.newsfeed.mvp.login.LoginView
import kotlinx.android.synthetic.main.fragment_login.*
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class LoginFragment: MvpAppCompatFragment(), LoginView, TextView.OnEditorActionListener {


    //MARK -
    companion object {
        fun getNewInstance() = LoginFragment().apply {
            arguments = Bundle().apply {

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
                Toast.makeText(context, "Введите допустимые данные", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //MARK - View implementation
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

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Log.e("ERROR", message)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            if (usernameInput.text.toString().isNotEmpty()) {
                passwordInput.requestFocus()
                return true
            }
        }
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            if (passwordInput.text.toString().isNotEmpty()) {
                if (usernameInput.text.toString() != "" && passwordInput.text.toString() != "") {
                    presenter.login(username = usernameInput.text.toString(), password = passwordInput.text.toString())
                } else {
                    Toast.makeText(context, "Введите допустимые данные", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return false
    }
}
