package com.arsoft.newsfeed.adapters

import android.graphics.Color
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.data.models.CommentModel
import com.arsoft.newsfeed.helpers.MyDateTimeFormatHelper
import com.arsoft.newsfeed.helpers.recycler.MultipleSpanGridLayoutManager
import com.arsoft.newsfeed.onClick.OnAttachmentClickListener
import com.arsoft.newsfeed.onClick.OnCommentsItemClickListener
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_comments.view.*
import java.util.*
import kotlin.collections.ArrayList

class CommentsRecyclerAdapter(private val onCommentsItemClickListener: OnCommentsItemClickListener, private val onAttachmentClickListener: OnAttachmentClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var commentsList = ArrayList<CommentModel>()

    fun setupComments(items: ArrayList<CommentModel>) {
        commentsList.clear()
        commentsList.addAll(items)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent,false)
        return CommentsViewHolder(itemView, onCommentsItemClickListener, onAttachmentClickListener)
    }

    override fun getItemCount(): Int {
        return commentsList.count()
    }

    override fun getItemId(position: Int): Long {
        return commentsList[position].itemId
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CommentsViewHolder).bind(commentsList[position])
    }

    class CommentsViewHolder(itemView: View, private val onCommentsItemClickListener: OnCommentsItemClickListener, private val onAttachmentClickListener: OnAttachmentClickListener): RecyclerView.ViewHolder(itemView) {

        private val avatarCircleImageView = itemView.findViewById<CircleImageView>(R.id.comment_avatar_imageview)
        private val nameTextView = itemView.findViewById<TextView>(R.id.comment_name_textview)
        private val commentTextTextView = itemView.findViewById<TextView>(R.id.comment_text_textview)
        private val dateTextView = itemView.findViewById<TextView>(R.id.comment_date_textview)
        private val threadCommentsRecyclerView = itemView.findViewById<RecyclerView>(R.id.thread_comments_recycler)
        private val commentReplyButton = itemView.findViewById<LinearLayout>(R.id.comment_reply_button)
        private val commentLikeButton = itemView.findViewById<LinearLayout>(R.id.comment_like_button)
        private val commentLikeItTextView = itemView.findViewById<TextView>(R.id.comment_likeit_textview)
        private val commentLikesCountTextView = itemView.findViewById<TextView>(R.id.comment_likes_count)
        private val commentLikeImageView = itemView.findViewById<ImageView>(R.id.comment_icon_imageview)
        private val attachmentsRecyclerView = itemView.findViewById<RecyclerView>(R.id.comment_attachments_recyclerview)


        private val attachmentsAdapter = AttachmentsRecyclerAdapter(onAttachmentClickListener)
        private lateinit var attachmentsLayoutManager: MultipleSpanGridLayoutManager
        private val adapter = CommentsRecyclerAdapter(onCommentsItemClickListener, onAttachmentClickListener)
        private val TYPE_COMMENT = "comment"

        fun bind(model: CommentModel) {


            if(!adapter.hasObservers()) {
                adapter.setHasStableIds(true)
            }
            threadCommentsRecyclerView.adapter = adapter
            threadCommentsRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            adapter.setupComments(model.thread)
            adapter.notifyDataSetChanged()

            Glide.with(itemView.context)
                .load(model.avatar)
                .into(avatarCircleImageView)
            nameTextView.text = model.name
            commentTextTextView.text = model.text

            val currentTime = Calendar.getInstance().timeInMillis
            dateTextView.text = MyDateTimeFormatHelper.timeFormat(
                postDate = model.date * 1000,
                currentTime = currentTime
            )

            if (model.userLikes != 0) {
                changeLikeButtonColorToAdded()
            } else {
                changeLikeButtonColorToDeleted()
            }

            if (model.likesCount > 0) {
                commentLikesCountTextView.text = model.likesCount.toString()
            } else {
                commentLikesCountTextView.text = ""
            }

            commentLikeButton.setOnClickListener {
                if (!model.isFavorite) {
                    onCommentsItemClickListener.onAddLikeClick(
                        type = TYPE_COMMENT,
                        ownerId = model.ownerId,
                        itemId = model.itemId,
                        viewItemId = itemId
                    )
                    Log.e("ITEMID", itemId.toString())
                    model.isFavorite = true
                    changeLikeButtonColorToAdded()
                } else {
                    onCommentsItemClickListener.onDeleteLikeClick(
                        type = TYPE_COMMENT,
                        ownerId = model.ownerId,
                        itemId = model.itemId,
                        viewItemId = itemId
                    )
                    Log.e("ITEMID", itemId.toString())
                    model.isFavorite = false
                    changeLikeButtonColorToDeleted()
                }
            }

            commentReplyButton.setOnClickListener {
                onCommentsItemClickListener.onReplyButtonClick(model = model, itemId = itemId)
            }

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
            attachmentsAdapter.setupAttachments(model.attachments)
            attachmentsAdapter.notifyDataSetChanged()



        }

        private fun changeLikeButtonColorToAdded() {
            commentLikeImageView.setImageResource(R.drawable.ic_favorite)
            commentLikeItTextView.setTextColor(itemView.resources.getColor(R.color.colorAccent))
            commentLikesCountTextView.setTextColor(itemView.resources.getColor(R.color.colorAccent))
        }

        private fun changeLikeButtonColorToDeleted() {
            commentLikeImageView.setImageResource(R.drawable.ic_favorite_border)
            commentLikeItTextView.setTextColor(Color.WHITE)
            commentLikesCountTextView.setTextColor(Color.WHITE)
        }
    }
}
