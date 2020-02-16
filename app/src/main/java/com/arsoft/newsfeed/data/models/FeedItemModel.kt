package com.arsoft.newsfeed.data.models

import android.os.Parcel
import android.os.Parcelable
import com.arsoft.newsfeed.data.newsfeed.request.Comments
import com.arsoft.newsfeed.data.newsfeed.request.Likes
import com.arsoft.newsfeed.data.newsfeed.request.Reposts
import com.arsoft.newsfeed.data.newsfeed.request.Views

data class FeedItemModel(
    val avatar: String,
    val sourceName: String,
    val postText: String,
    val attachments: ArrayList<IAttachment>,
    val date: Long,
    val likes: Likes,
    val comments: Comments,
    val reposts: Reposts,
    val views: Views,
    val ownerId: Long,
    val postId: Long,
    var isFavorite: Boolean,
    var startFrom: String
)
