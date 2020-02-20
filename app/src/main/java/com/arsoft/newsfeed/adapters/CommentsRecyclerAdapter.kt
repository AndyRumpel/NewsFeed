package com.arsoft.newsfeed.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsoft.newsfeed.R
import com.arsoft.newsfeed.data.models.CommentModel
import com.arsoft.newsfeed.helpers.MyDateTimeFormatHelper
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class CommentsRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var commentsList = ArrayList<CommentModel>()

    fun setupComments(items: ArrayList<CommentModel>) {
        commentsList.clear()
        commentsList.addAll(items)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent,false)
        return CommentsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return commentsList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CommentsViewHolder).bind(commentsList[position])
    }

    class CommentsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val avatarCircleImageView = itemView.findViewById<CircleImageView>(R.id.comment_avatar_imageview)
        private val nameTextView = itemView.findViewById<TextView>(R.id.comment_name_textview)
        private val commentTextTextView = itemView.findViewById<TextView>(R.id.comment_text_textview)
        private val dateTextView = itemView.findViewById<TextView>(R.id.comment_date_textview)
        private val threadCommentsRecyclerView = itemView.findViewById<RecyclerView>(R.id.thread_comments_recycler)

        private val adapter = CommentsRecyclerAdapter()

        fun bind(model: CommentModel) {
            Glide.with(itemView.context)
                .load(model.avatar)
                .into(avatarCircleImageView)
            nameTextView.text = model.name
            commentTextTextView.text = model.text

            val currentTime = Calendar.getInstance().timeInMillis
            dateTextView.text = MyDateTimeFormatHelper.timeFormat(postDate = model.date * 1000, currentTime = currentTime)

            threadCommentsRecyclerView.adapter = adapter
            threadCommentsRecyclerView.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            adapter.setupComments(model.thread)
            adapter.notifyDataSetChanged()

        }
    }
}