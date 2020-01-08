package com.arsoft.newsfeed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.arsoft.newsfeed.R
import com.bumptech.glide.Glide

class PhotoViewPagerAdapter: PagerAdapter() {

    private val photos: ArrayList<String> = ArrayList()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object` as View

    override fun getCount(): Int = photos.count()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = LayoutInflater.from(container.context).inflate(R.layout.viewpager_item, container, false)
        val imageView = layout.findViewById<ImageView>(R.id.photo_image)
        Glide.with(container.context)
            .load(photos[position])
            .into(imageView)

        container.addView(imageView)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun setupPhotos(photos: ArrayList<String>){
        photos.addAll(photos)
    }
}