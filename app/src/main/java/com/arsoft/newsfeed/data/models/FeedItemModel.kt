package com.arsoft.newsfeed.data.models

import android.os.Parcel
import android.os.Parcelable
import com.arsoft.newsfeed.data.newsfeed.request.Comments
import com.arsoft.newsfeed.data.newsfeed.request.Likes
import com.arsoft.newsfeed.data.newsfeed.request.Reposts
import com.arsoft.newsfeed.data.newsfeed.request.Views

data class FeedItemModel(
    val avatar: String?,
    val sourceName: String?,
    val postText: String?,
    val attachments: ArrayList<IAttachment>,
    val date: Long,
    val likes: Likes,
    val comments: Comments,
    val reposts: Reposts,
    val views: Views,
    val ownerId: Long,
    val postId: Long,
    var isFavorite: Boolean,
    var startFrom: String?,
    val copyHistory: ArrayList<RepostModel>,
    val canComment: Boolean
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("attachments"),
        parcel.readLong(),
        TODO("likes"),
        TODO("comments"),
        TODO("reposts"),
        TODO("views"),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        TODO("copyHistory"),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(avatar)
        parcel.writeString(sourceName)
        parcel.writeString(postText)
        parcel.writeLong(date)
        parcel.writeLong(ownerId)
        parcel.writeLong(postId)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeString(startFrom)
        parcel.writeByte(if (canComment) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeedItemModel> {
        override fun createFromParcel(parcel: Parcel): FeedItemModel {
            return FeedItemModel(parcel)
        }

        override fun newArray(size: Int): Array<FeedItemModel?> {
            return arrayOfNulls(size)
        }
    }
}