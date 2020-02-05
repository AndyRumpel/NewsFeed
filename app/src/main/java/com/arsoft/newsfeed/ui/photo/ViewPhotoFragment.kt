package com.arsoft.newsfeed.ui.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.adapters.ViewPhotoViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_view_photo.*

class ViewPhotoFragment: MvpAppCompatFragment() {

    private lateinit var adapter: ViewPhotoViewPagerAdapter

    companion object{
        fun getNewInstance(photoURLs: ArrayList<String?>, position: Int) = ViewPhotoFragment().apply {
            arguments = Bundle().apply {
                putStringArrayList("photo_urls", photoURLs)
                putInt("position", position)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
4

        adapter = ViewPhotoViewPagerAdapter()
        view_pager.adapter = adapter

        if (arguments != null) {

            val photoURLs = arguments!!.getStringArrayList("photo_urls")!!
            val position = arguments!!.getInt("position")

            adapter.setupPhotos(photoURLs)
            view_pager.setCurrentItem(position, false)
            adapter.notifyDataSetChanged()

            circle_indicator.setViewPager(view_pager)

            if (photoURLs.count() <= 1) {
                circle_indicator.visibility = View.INVISIBLE
            }

        }



    }

}