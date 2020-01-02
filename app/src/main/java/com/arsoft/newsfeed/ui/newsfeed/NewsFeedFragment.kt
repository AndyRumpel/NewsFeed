package com.arsoft.newsfeed.ui.newsfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arsoft.newsfeed.R
import kotlinx.android.synthetic.main.fragment_newsfeed.*

class NewsFeedFragment: MvpAppCompatFragment() {

    companion object{
        fun getNewInstance(accessToken: String) = NewsFeedFragment().apply {
            arguments = Bundle().apply {
                putString("access_token", accessToken)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_newsfeed, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            text.text = arguments!!.getString("access_token")
        }
    }


}