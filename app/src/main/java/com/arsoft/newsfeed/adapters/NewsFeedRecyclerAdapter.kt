package com.arsoft.newsfeed.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import at.blogc.android.views.ExpandableTextView
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.data.models.FeedItemModel
import com.arsoft.newsfeed.helpers.MyDateTimeFormatHelper
import com.arsoft.newsfeed.helpers.recycler.MultipleSpanGridLayoutManager
import com.arsoft.newsfeed.onClick.OnAttachmentClickListener
import com.arsoft.newsfeed.onClick.OnNewsFeedItemClickListener
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList


class NewsFeedRecyclerAdapter(private val onNewsFeedItemClickListener: OnNewsFeedItemClickListener, private val onAttachmentClickListener: OnAttachmentClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val newsFeedList: ArrayList<FeedItemModel> = ArrayList()
    private lateinit var loadMoreOwner: LoadMoreOwner

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        return NewsFeedViewHolder(itemView, onNewsFeedItemClickListener, onAttachmentClickListener)
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

    fun addMoreNewsFeedListItems(items: ArrayList<FeedItemModel>) {
        newsFeedList.addAll(items)
    }

    fun setLoadMoreOwner(loadMoreOwner: LoadMoreOwner) {
        this.loadMoreOwner = loadMoreOwner
    }

    override fun getItemId(position: Int): Long {
        return newsFeedList[position].postId
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val layoutPosition = holder.layoutPosition
        if (layoutPosition == this.itemCount - 10) {
            loadMoreOwner.loadMore(newsFeedList[layoutPosition].startFrom!!)
        }
    }

    interface LoadMoreOwner {
        fun loadMore(startFrom: String)
    }

    class NewsFeedViewHolder(itemView: View, private val onNewsFeedItemClickListener: OnNewsFeedItemClickListener, private val onAttachmentClickListener: OnAttachmentClickListener) : RecyclerView.ViewHolder(itemView){


        private val avatarImageView= itemView.findViewById<ImageView>(R.id.post_avatar)
        private val sourceNameTextView = itemView.findViewById<TextView>(R.id.post_source_name)
        private val postTextTextView = itemView.findViewById<ExpandableTextView>(R.id.post_expandable_text_view)
        private val showMoreTextView = itemView.findViewById<TextView>(R.id.show_more_text_view)
        private val attachmentsRecyclerView = itemView.findViewById<RecyclerView>(R.id.attachments_recycler_view)
        private val dateTextView = itemView.findViewById<TextView>(R.id.post_date_time)
        private val likesButton = itemView.findViewById<ImageButton>(R.id.likes_button)
        private val commentsButton = itemView.findViewById<ImageButton>(R.id.comments_button)
        private val likesCountTextView = itemView.findViewById<TextView>(R.id.likes_count_textview)
        private val commentsCountTextView = itemView.findViewById<TextView>(R.id.comments_count_textview)
        private val repostsCountTextView = itemView.findViewById<TextView>(R.id.reposts_count_textview)
        private val viewsCountTextView = itemView.findViewById<TextView>(R.id.views_count_textview)
        private val repostItem = itemView.findViewById<ConstraintLayout>(R.id.repost_item)
        private val repostAvatarImageView = itemView.findViewById<CircleImageView>(R.id.repost_avatar)
        private val repostNameTextView = itemView.findViewById<TextView>(R.id.repost_source_name)
        private val repostDateTextView = itemView.findViewById<TextView>(R.id.repost_date_time)

        private val attachmentsAdapter = AttachmentsRecyclerAdapter(onAttachmentClickListener)
        private lateinit var attachmentsLayoutManager: MultipleSpanGridLayoutManager
        private var currentTime = 0L
        private val TYPE_POST = "post"


        fun bind(model: FeedItemModel) {

            attachmentsLayoutManager =
                MultipleSpanGridLayoutManager(
                    context = itemView.context,
                    spanCount = 4,
                    items = model.attachments
                )


            attachmentsRecyclerView.isNestedScrollingEnabled = false
            attachmentsRecyclerView.layoutManager = attachmentsLayoutManager
            attachmentsRecyclerView.adapter = attachmentsAdapter
            attachmentsRecyclerView.setHasFixedSize(true)
            attachmentsAdapter.setupAttachments(attachments = model.attachments)
            attachmentsAdapter.notifyDataSetChanged()


            if (model.avatar!!.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(model.avatar)
                    .into(avatarImageView)
            }
            sourceNameTextView.text = model.sourceName
            postTextTextView.text = model.postText

            postTextTextView.post {
                if (postTextTextView.lineCount >= 10) {
                    showMoreTextView.visibility = View.VISIBLE
                    showMoreTextView.textSize = 14F
                } else {
                    showMoreTextView.visibility = View.INVISIBLE
                    showMoreTextView.textSize = 0F
                }
            }

            showMoreTextView.setOnClickListener {
                if (postTextTextView.isExpanded) {
                    postTextTextView.collapse()
                    showMoreTextView.text = itemView.resources.getText(R.string.show_more_text)
                } else {
                    postTextTextView.expand()
                    showMoreTextView.text = itemView.resources.getText(R.string.hide_text)
                }
            }

            currentTime = Calendar.getInstance().timeInMillis
            dateTextView.text = MyDateTimeFormatHelper.timeFormat(postDate = model.date * 1000, currentTime = currentTime)

            setupLikes(model = model)

            commentsButton.setOnClickListener{
                onNewsFeedItemClickListener.onCommentsButtonClick(model = model)
            }

            if (!model.copyHistory.isNullOrEmpty()) {
                repostItem.visibility = View.VISIBLE
                with(model.copyHistory.first()){

                    Glide.with(itemView.context)
                        .load(avatar)
                        .into(repostAvatarImageView)

                    repostNameTextView.text = sourceName
                    repostDateTextView.text = MyDateTimeFormatHelper.timeFormat(postDate = date * 1000, currentTime = currentTime)
                    postTextTextView.text = postText

                    postTextTextView.post {
                        if (postTextTextView.lineCount >= 10) {
                            showMoreTextView.visibility = View.VISIBLE
                            showMoreTextView.textSize = 14F
                        } else {
                            showMoreTextView.visibility = View.INVISIBLE
                            showMoreTextView.textSize = 0F
                        }
                    }

                    showMoreTextView.setOnClickListener {
                        if (postTextTextView.isExpanded) {
                            postTextTextView.collapse()
                            showMoreTextView.text = itemView.resources.getText(R.string.show_more_text)
                        } else {
                            postTextTextView.expand()
                            showMoreTextView.text = itemView.resources.getText(R.string.hide_text)
                        }
                    }

                    attachmentsLayoutManager =
                        MultipleSpanGridLayoutManager(
                            context = itemView.context,
                            spanCount = 4,
                            items = attachments
                        )
                    attachmentsRecyclerView.isNestedScrollingEnabled = false
                    attachmentsRecyclerView.layoutManager = attachmentsLayoutManager
                    attachmentsRecyclerView.adapter = attachmentsAdapter
                    attachmentsRecyclerView.setHasFixedSize(true)
                    attachmentsAdapter.setupAttachments(attachments = attachments)
                    attachmentsAdapter.notifyDataSetChanged()
                }
            } else {
                repostItem.visibility = View.GONE
            }
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

            if (model.views.count != null && model.views.count > 0) {
                viewsCountTextView.text = model.views.count.toString()
            } else {
                viewsCountTextView.text = ""
            }



            likesButton.setOnClickListener {
                if (!model.isFavorite) {
                    onNewsFeedItemClickListener.onAddLikeClick(
                        type = TYPE_POST,
                        ownerId = model.ownerId,
                        itemId = model.postId,
                        viewItemId = itemId)
                    model.isFavorite = true
                    likesButton.setImageResource(R.drawable.ic_favorite)
                    if (android.os.Build.VERSION.SDK_INT >= 23) {
                        likesCountTextView.setTextColor(itemView.context.getColor(R.color.colorAccent))
                    } else {
                        likesCountTextView.setTextColor(itemView.context.resources.getColor(R.color.colorAccent))
                    }

                } else {
                    onNewsFeedItemClickListener.onDeleteLikeClick(
                        type = TYPE_POST,
                        ownerId = model.ownerId,
                        itemId = model.postId,
                        viewItemId = itemId
                    )
                    model.isFavorite = false
                    likesButton.setImageResource(R.drawable.ic_favorite_border)
                    likesCountTextView.setTextColor(Color.WHITE)
                }
            }
        }

    }
}