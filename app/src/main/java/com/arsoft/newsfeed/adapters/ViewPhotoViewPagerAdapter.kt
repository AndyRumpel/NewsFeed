package com.arsoft.newsfeed.adapters

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.R
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView

class ViewPhotoViewPagerAdapter: RecyclerView.Adapter<ViewPhotoViewPagerAdapter.ViewPagerVH>() {


    private val photos: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerVH =
        ViewPagerVH(LayoutInflater.from(parent.context).inflate(R.layout.viewpager_item, parent, false))


    override fun getItemCount(): Int = photos.count()

    override fun onBindViewHolder(holder: ViewPagerVH, position: Int) {
        holder.bind(photo = photos[position])
    }

    fun setupPhotos(photoURLs: ArrayList<String>){
        photos.addAll(photoURLs)
    }

    class ViewPagerVH(itemView: View): RecyclerView.ViewHolder(itemView){

        private val imageView = itemView.findViewById<PhotoView>(R.id.photo_image)


        fun bind(photo: String) {

            Glide.with(itemView.context)
                .load(photo)
                .into(imageView)
        }

    }
}