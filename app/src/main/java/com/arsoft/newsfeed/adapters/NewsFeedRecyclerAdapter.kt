package com.arsoft.newsfeed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.MyDateTimeFormatHelper
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.app.NewsFeedApplication
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.data.models.SourceModel
import com.arsoft.newsfeed.data.newsfeed.request.NewsFeedResponse
import com.arsoft.newsfeed.ui.screens.Screens
import com.bumptech.glide.Glide
import com.lopei.collageview.CollageView
import com.ms.square.android.expandabletextview.ExpandableTextView
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.abs


class NewsFeedRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ATTACHMENTS_TYPE_PHOTO = "photo"
    private val ATTACHMENTS_TYPE_VIDEO = "video"
    private val PHOTO_SIZE_WHERE_MAX_SIDE_807PX = 'y'
    private val PHOTO_SIZE_WHERE_MAX_SIDE_604PX = 'x'
    private val VIDEO_PREVIEW_IMAGE_WIDTH = 720

    private val newsFeedList: ArrayList<FeedItemModel> = ArrayList()
    private val sourceProfiles: ArrayList<SourceModel> = ArrayList()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
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

        var photoURLs = ArrayList<String>()
        var videoPreviewImage = ""
        var videoDuration = 0
        var videoID = ""
        var videoOwnerID = ""
        var avatar = ""
        var name = ""
        var postText = ""
        val currentTime = Calendar.getInstance().timeInMillis

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
        fun resetVariables() {
            photoURLs = ArrayList()
            videoPreviewImage = ""
            videoDuration = 0
            videoID = ""
            videoOwnerID = ""
            avatar = ""
            name = ""
            postText = ""
        }

        for (item in items.response.items) {
            for (source in sourceProfiles) {
                if (abs(item.source_id) == source.id) {
                    if (!item.attachments.isNullOrEmpty()) {
                        resetVariables()
                        for (attachment in item.attachments) {
                            if (attachment.type == ATTACHMENTS_TYPE_PHOTO) {
                                for (size in attachment.photo!!.sizes) {
                                    if (size.type == PHOTO_SIZE_WHERE_MAX_SIDE_604PX) {
                                        photoURLs.add(size.url)
                                    }
                                }
                                avatar = source.avatar
                                name = source.name
                                postText = item.text
                            }
                            if (attachment.type == ATTACHMENTS_TYPE_VIDEO) {
                                for (image in attachment.video!!.image) {
                                    if (image.width == VIDEO_PREVIEW_IMAGE_WIDTH) {
                                        videoPreviewImage = image.url
                                        videoDuration = attachment.video.duration
                                        videoOwnerID = attachment.video.owner_id.toString()
                                        videoID = "${videoOwnerID}_${attachment.video.id}"
                                    }
                                }
                            }
                        }

                        if (avatar != "" && name != "" && postText != "") {

                            newsFeedList.add(
                                FeedItemModel(
                                    avatar = source.avatar,
                                    sourceName = source.name,
                                    postText = item.text,
                                    photoURLs = photoURLs,
                                    videoPreviewImage = videoPreviewImage,
                                    videoDuration = videoDuration,
                                    videoID = videoID,
                                    videoOwnerID = videoOwnerID,
                                    date = MyDateTimeFormatHelper.timeFormat(item.date * 1000, currentTime)))
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
        private val postTextTxt = itemView.findViewById<ExpandableTextView>(R.id.post_expandable_text_view)
        private val collageView = itemView.findViewById<CollageView>(R.id.collage_view)
        private val dateTextView = itemView.findViewById<TextView>(R.id.post_date_time)
        private val videoPreviewImage = itemView.findViewById<ImageView>(R.id.video_preview_image)
        private val videoPreviewDurationTextView = itemView.findViewById<TextView>(R.id.video_duration_text_view)






        fun bind(model: FeedItemModel) {

            NewsFeedApplication.INSTANCE.getAppComponent()!!.inject(this)

            if (model.avatar.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(model.avatar)
                    .into(avatarImg)
            }
            sourceNameTxt.text = model.sourceName

            collageView
                .photoMargin(1)
                .defaultPhotosForLine(4)
                .useFirstAsHeader(false)
                .photosForm(CollageView.ImageForm.IMAGE_FORM_SQUARE)
                .useCards(true)
                .loadPhotos(model.photoURLs)
            collageView.setOnPhotoClickListener { position ->
                router.navigateTo(Screens.ViewPhotoScreen(photoURLs = model.photoURLs!!,position =  position))
            }

            if (model.videoPreviewImage != null && model.videoDuration != 0) {
                Glide.with(itemView.context)
                    .load(model.videoPreviewImage)
                    .into(videoPreviewImage)

                videoPreviewDurationTextView.text = "${model.videoDuration!!.div(60)}:${model.videoDuration%60}"

                videoPreviewImage.setOnClickListener {
                    router.navigateTo(Screens.VideoPlayerScreen(videoID = model.videoID!!, videoOwnerID = model.videoOwnerID!!))
                }
            }



            if (model.postText != "") {
                postTextTxt.text = model.postText
            }

            dateTextView.text = model.date
        }
    }
}