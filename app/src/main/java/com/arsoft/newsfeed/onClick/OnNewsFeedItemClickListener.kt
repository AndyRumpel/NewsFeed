package com.arsoft.newsfeed.onClick

import com.arsoft.newsfeed.data.models.FeedItemModel

interface OnNewsFeedItemClickListener {
    fun onAddLikeClick(type: String, ownerId: Long, itemId: Long, viewItemId: Long)
    fun onDeleteLikeClick(type: String, ownerId: Long, itemId: Long, viewItemId: Long)
    fun onCommentsButtonClick(model: FeedItemModel)
}