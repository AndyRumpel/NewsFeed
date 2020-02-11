package com.arsoft.newsfeed.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.helpers.MyDateTimeFormatHelper
import com.arsoft.newsfeed.helpers.recycler.MultipleSpanGridLayoutManager
import com.bumptech.glide.Glide
import com.ms.square.android.expandabletextview.ExpandableTextView
import java.util.*
import kotlin.collections.ArrayList


class NewsFeedRecyclerAdapter(private val onNewsFeedItemClickListener: NewsFeedViewHolder.OnNewsFeedItemClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val newsFeedList: ArrayList<FeedItemModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        return NewsFeedViewHolder(itemView, onNewsFeedItemClickListener)
    }

    override fun getItemCount(): Int {
        return newsFeedList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NewsFeedViewHolder).bind(newsFeedList[position])
    }



    fun getItem(position: Int): FeedItemModel = newsFeedList[position]


    fun setupNewsFeedList(items: ArrayList<FeedItemModel>){
        newsFeedList.clear()
        newsFeedList.addAll(items)
    }

    class NewsFeedViewHolder(itemView: View, private val onNewsFeedItemClickListener: OnNewsFeedItemClickListener) : RecyclerView.ViewHolder(itemView){


        private val avatarImg= itemView.findViewById<ImageView>(R.id.post_avatar)
        private val sourceNameTxt = itemView.findViewById<TextView>(R.id.post_source_name)
        private val postTextTxt = itemView.findViewById<ExpandableTextView>(R.id.post_expandable_text_view)
        private val attachmentsRecyclerView = itemView.findViewById<RecyclerView>(R.id.attachments_recycler_view)
        private val dateTextView = itemView.findViewById<TextView>(R.id.post_date_time)
        private val likesButton = itemView.findViewById<ImageButton>(R.id.likes_button)
        private val commentsButton = itemView.findViewById<ImageButton>(R.id.comments_button)
        private val likesCountTextView = itemView.findViewById<TextView>(R.id.likes_count_textview)
        private val commentsCountTextView = itemView.findViewById<TextView>(R.id.comments_count_textview)
        private val repostsCountTextView = itemView.findViewById<TextView>(R.id.reposts_count_textview)
        private val viewsCountTextView = itemView.findViewById<TextView>(R.id.views_count_textview)

        private val adapter = AttachmentsRecyclerAdapter(onNewsFeedItemClickListener)
        private lateinit var layoutManager: MultipleSpanGridLayoutManager
        private var currentTime = 0L


        fun bind(model: FeedItemModel) {
//            Log.e("ATTACHMENTS", model.attachments.toString())

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


            if (model.postText != "") {
                postTextTxt.text = model.postText
            }

            currentTime = Calendar.getInstance().timeInMillis
            dateTextView.text = MyDateTimeFormatHelper.timeFormat(postDate = model.date * 1000, currentTime = currentTime)

            setupLikes(model = model)


        }

        private fun setupLikes(model: FeedItemModel) {
            when(model.likes.user_likes) {
                0 -> likesButton.setImageResource(R.drawable.ic_favorite_border)
                1 -> likesButton.setImageResource(R.drawable.ic_favorite)
            }

            if (model.likes.count > 0) {
                likesCountTextView.text = model.likes.count.toString()
            } else {
                likesCountTextView.text = ""
            }

            if (model.comments.count > 0) {
                commentsCountTextView.text = model.comments.count.toString()
            } else {
                commentsCountTextView.text = ""
            }

            if (model.reposts.count > 0) {
                repostsCountTextView.text = model.reposts.count.toString()
            } else {
                repostsCountTextView.text = ""
            }

            if (model.views.count > 0) {
                viewsCountTextView.text = model.views.count.toString()
            } else {
                viewsCountTextView.text = ""
            }



            likesButton.setOnClickListener {
                if (!model.isFavorite) {
                    onNewsFeedItemClickListener.onAddLikeClick(
                        ownerId = model.ownerId,
                        itemId = model.postId,
                        position = adapterPosition)
                    model.isFavorite = true
                    likesButton.setImageResource(R.drawable.ic_favorite)
                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        likesCountTextView.setTextColor(itemView.context.getColor(R.color.colorAccent))
                    } else {
                        likesCountTextView.setTextColor(itemView.context.resources.getColor(R.color.colorAccent))
                    }

                } else {
                    onNewsFeedItemClickListener.onDeleteLikeClick(
                        ownerId = model.ownerId,
                        itemId = model.postId,
                        position = adapterPosition
                    )
                    model.isFavorite = false
                    likesButton.setImageResource(R.drawable.ic_favorite_border)
                    likesCountTextView.setTextColor(Color.WHITE)
                }
            }
        }

        interface OnNewsFeedItemClickListener{
            fun onPhotoClick(photoURLs: ArrayList<String?> ,position: Int)
            fun onVideoClick(videoID: String, videoOwnerID: String)
            fun onAddLikeClick(ownerId: Long, itemId: Long, position: Int)
            fun onDeleteLikeClick(ownerId: Long, itemId: Long, position: Int)
        }

    }
}