package com.arsoft.newsfeed.onClick

import com.arsoft.newsfeed.data.models.CommentModel

interface OnCommentsItemClickListener {
    fun onAddLikeClick(type: String, ownerId: Long, itemId: Long, viewItemId: Long)
    fun onDeleteLikeClick(type: String, ownerId: Long, itemId: Long, viewItemId: Long)
    fun onReplyButtonClick(model: CommentModel, itemId: Long)
}