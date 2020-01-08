package com.arsoft.newsfeed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.data.models.SourceModel
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedResponse
import com.arsoft.newsfeed.ui.screens.Screens
import com.bumptech.glide.Glide
import com.lopei.collageview.CollageView
import ru.terrakok.cicerone.Router
import javax.inject.Inject
import kotlin.math.abs


class NewsFeedRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ATTACHMENTS_TYPE_PHOTO = "photo"

    private val newsFeedList: ArrayList<FeedItemModel> = ArrayList()
    private val sourceProfiles: ArrayList<SourceModel> = ArrayList()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.feed_item, parent, false)
        return NewsFeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return newsFeedList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsFeedViewHolder).bind(newsFeedList[position])
    }

    fun setupNewsFeedList(items: NewsFeedResponse){
        newsFeedList.clear()
        sourceProfiles.clear()

        var photoURLs: ArrayList<String>
        var avatar = ""
        var name = ""
        var postText = ""

        for (item in items.response.profiles) {
            sourceProfiles.add(
                SourceModel(
                    id = item.id,
                    name = "${item.first_name} ${item.last_name}",
                    avatar = item.photo_100)
            )
        }

        for (item in items.response.groups) {
            sourceProfiles.add(
                SourceModel(
                    id = item.id,
                    name = item.name,
                    avatar = item.photo_100))
        }

        for (item in items.response.items) {
            for (source in sourceProfiles) {
                if (abs(item.source_id) == source.id) {
                    if (!item.attachments.isNullOrEmpty()) {
                        photoURLs = ArrayList()
                        for (attachment in item.attachments) {
                            if (attachment.type == ATTACHMENTS_TYPE_PHOTO) {
                                for (size in attachment.photo.sizes) {
                                    if (size.type == attachment.photo.sizes.last().type) {
                                        photoURLs.add(size.url)

                                    }
                                }
                                avatar = source.avatar
                                name = source.name
                                postText = item.text
                            }
                        }

                        if (avatar != "" && name != "" && postText != "") {
                        newsFeedList.add(
                            FeedItemModel(
                                avatar = source.avatar,
                                sourceName = source.name,
                                postText = item.text,
                                photoURLs = photoURLs))
                        }
                    }
                }
            }
        }
    }

    class NewsFeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @Inject
        lateinit var router: Router

        private val avatarImg= itemView.findViewById<ImageView>(R.id.post_avatar)
        private val sourceNameTxt = itemView.findViewById<TextView>(R.id.post_source_name)
        private val postTextTxt = itemView.findViewById<TextView>(R.id.post_text)
        private val collageView = itemView.findViewById<CollageView>(R.id.collage_view)






        fun bind(model: FeedItemModel) {

            NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(this)

            if (model.avatar.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(model.avatar)
                    .into(avatarImg)
            }
            sourceNameTxt.text = model.sourceName

            collageView
                .photoMargin(5)
                .defaultPhotosForLine(5)
                .useFirstAsHeader(true)
                .headerForm(CollageView.ImageForm.IMAGE_FORM_HALF_HEIGHT)
                .useCards(true)
                .loadPhotos(model.photoURLs)

            collageView.setOnPhotoClickListener { position ->
                router.navigateTo(Screens.ViewPhotoScreen(photoURLs = model.photoURLs,position =  position))
            }

            if (model.postText != "") {
                postTextTxt.text = model.postText
            }
        }
    }
}