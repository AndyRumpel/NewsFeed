package com.arsoft.newsfeed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.helpers.recycler.AttachmentsItemDecoration
import com.arsoft.newsfeed.helpers.recycler.MultipleSpanGridLayoutManager
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import kotlin.collections.ArrayList


class NewsFeedRecyclerAdapter(private val onAttachmentClickListener: AttachmentsRecyclerAdapter.OnAttachmentClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val newsFeedList: ArrayList<FeedItemModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        return NewsFeedViewHolder(itemView, onAttachmentClickListener)
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

    class NewsFeedViewHolder(itemView: View, onAttachmentClickListener: AttachmentsRecyclerAdapter.OnAttachmentClickListener) : RecyclerView.ViewHolder(itemView){


        private val avatarImg= itemView.findViewById<ImageView>(R.id.post_avatar)
        private val sourceNameTxt = itemView.findViewById<TextView>(R.id.post_source_name)
        private val postTextTxt = itemView.findViewById<ExpandableTextView>(R.id.post_expandable_text_view)
        private val attachmentsRecyclerView = itemView.findViewById<RecyclerView>(R.id.attachments_recycler_view)
        private val dateTextView = itemView.findViewById<TextView>(R.id.post_date_time)

        private val adapter = AttachmentsRecyclerAdapter(onAttachmentClickListener)
        private lateinit var layoutManager: MultipleSpanGridLayoutManager


        fun bind(model: FeedItemModel) {


            layoutManager =
                MultipleSpanGridLayoutManager(
                    context = itemView.context,
                    spanCount = 4,
                    items = model.attachments
                )
            attachmentsRecyclerView.isNestedScrollingEnabled = false
            attachmentsRecyclerView.layoutManager = layoutManager
            attachmentsRecyclerView.adapter = adapter
            attachmentsRecyclerView.setHasFixedSize(true)
            adapter.setupAttachments(attachments = model.attachments)
            adapter.notifyDataSetChanged()



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


}