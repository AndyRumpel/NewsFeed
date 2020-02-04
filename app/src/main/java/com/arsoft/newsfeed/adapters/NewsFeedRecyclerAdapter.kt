package com.arsoft.newsfeed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import kotlin.collections.ArrayList


class NewsFeedRecyclerAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val newsFeedList: ArrayList<FeedItemModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        return NewsFeedViewHolder(itemView, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return newsFeedList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsFeedViewHolder).bind(newsFeedList[position])
    }

    fun setupNewsFeedList(items: ArrayList<FeedItemModel>){
        newsFeedList.clear()
        newsFeedList.addAll(items)
    }

    class NewsFeedViewHolder(itemView: View, onItemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){


        private val avatarImg= itemView.findViewById<ImageView>(R.id.post_avatar)
        private val sourceNameTxt = itemView.findViewById<TextView>(R.id.post_source_name)
        private val postTextTxt = itemView.findViewById<ExpandableTextView>(R.id.post_expandable_text_view)
        private val attachmentsRecyclerView = itemView.findViewById<RecyclerView>(R.id.attachments_recycler_view)
        private val dateTextView = itemView.findViewById<TextView>(R.id.post_date_time)
//        private val videoPreviewImage = itemView.findViewById<ImageView>(R.id.video_preview_image)
//        private val videoPreviewDurationTextView = itemView.findViewById<TextView>(R.id.video_duration_text_view)

        private val adapter = AttachmentsRecyclerAdapter()
        private val layoutMaganer = GridLayoutManager(itemView.context, 4)


        fun bind(model: FeedItemModel) {

            layoutMaganer.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    when (model.attachments.count()) {
                        1 -> return 4
                        2 -> return 2
                        3 -> return when (position) {
                                0 -> 4
                                else -> 2
                        }
                        4 -> return 1
                        5 -> return when (position) {
                                0 -> 1
                                else -> 1
                        }
                        6 -> return when(position) {
                                0, 1 -> 2
                                else -> 1
                        }
                        7 -> return when (position) {
                                0 -> 4
                                1, 2 -> 2
                                else -> 1
                        }
                        8 -> return 1
                        9 -> return when(position) {
                                0 -> 4
                                else -> 1
                        }
                        10 -> return when(position) {
                                0, 1 -> 2
                                else -> 1
                        }
                        else -> return 1
                    }

                }

            }

            attachmentsRecyclerView.isNestedScrollingEnabled = false
            attachmentsRecyclerView.adapter = adapter
            attachmentsRecyclerView.layoutManager = layoutMaganer
            adapter.setupAttachments(attachments = model.attachments!!)
            adapter.notifyDataSetChanged()

//            Log.e("ATTACHMENTS", model.attachments.toString())


            if (model.avatar.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(model.avatar)
                    .into(avatarImg)
            }
            sourceNameTxt.text = model.sourceName



//            if (model.videoPreviewImage != null && model.videoDuration != null
//                && model.videoID != null && model.videoOwnerID != null) {
//                Glide.with(itemView.context)
//                    .load(model.videoPreviewImage)
//                    .into(videoPreviewImage)
//
//                var videoDurationInSeconds = model.videoDuration % 60
//                var videoDurationInMinutes = model.videoDuration.div(60)
//
//
//                videoPreviewDurationTextView.text =
//                    String.format("%02d", videoDurationInMinutes) + ":" + String.format(
//                        "%02d",
//                        videoDurationInSeconds
//                    )
//
//
//                videoPreviewImage.setOnClickListener {
//                    onItemClickListener.onVideoClick(
//                        videoID = model.videoID!!,
//                        videoOwnerID = model.videoOwnerID!!
//                    )
//                }
//            }


            if (model.postText != "") {
                postTextTxt.text = model.postText
            }

            dateTextView.text = model.date
        }
    }

    interface OnItemClickListener{
        fun onPhotoClick(photoURLs: ArrayList<String> ,position: Int)
        fun onVideoClick(videoID: String, videoOwnerID: String)
    }
}