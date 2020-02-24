package com.arsoft.newsfeed.data.comments.repository

import android.util.Log
import com.arsoft.newsfeed.data.comments.request.Attachment
import com.arsoft.newsfeed.data.comments.request.CommentsResponse
import com.arsoft.newsfeed.data.comments.request.CommentsService
import com.arsoft.newsfeed.data.models.*
import kotlin.math.abs

class CommentsRepository(private val apiService: CommentsService) {

    private val VERSION = "5.103"
    private val EXTENDED_RESPONSE = 1
    private val NEED_LIKES = 1
    private val THREAD_ITEMS_COUNT = 10
    private val COMMENTS_COUNT = 100

    private val ATTACHMENTS_TYPE_PHOTO = "photo"
    private val ATTACHMENTS_TYPE_VIDEO = "video"
    private val ATTACHMENTS_TYPE_STICKER = "sticker"


    suspend fun loadComments(ownerId: Long, postId: Long, accessToken: String): ArrayList<CommentModel>{
        val result = apiService.getComments(
            ownerId = ownerId,
            accessToken = accessToken,
            post_id = postId,
            version = VERSION,
            extended = EXTENDED_RESPONSE,
            needLikes = NEED_LIKES,
            threadItemsCount = THREAD_ITEMS_COUNT,
            count = COMMENTS_COUNT
        ).await()

        return parseData(result)
    }

    private fun parseData(result: CommentsResponse): java.util.ArrayList<CommentModel> {
        var name = ""
        var avatar = ""
        var date = 0L
        var text = ""
        val attachments = ArrayList<IAttachment>()
        val sourceProfiles = ArrayList<SourceModel>()
        val commentsList = ArrayList<CommentModel>()
        var threadComments = ArrayList<CommentModel>()
        var threadCommentName = ""
        var threadCommentAvatar = ""
        var threadCommentAttachments = ArrayList<IAttachment>()
        var isFavorite: Boolean

        for (source in result.response.profiles) {
            sourceProfiles.add(
                SourceModel(
                    id = source.id,
                    name = "${source.first_name} ${source.last_name}",
                    avatar = source.photo_100
                )
            )
        }

        for (source in result.response.groups) {
            sourceProfiles.add(
                SourceModel(
                    id = source.id,
                    name = source.name,
                    avatar = source.photo_100
                )
            )
        }

        for (item in result.response.items) {
            for (source in sourceProfiles) {
                if(item.from_id == source.id) {
                    name = source.name
                    avatar = source.avatar
                }
            }

            text = item.text
            date = item.date

            if (!item.attachments.isNullOrEmpty()) {
                for (attachment in item.attachments) {
                    when (attachment.type) {
                        ATTACHMENTS_TYPE_STICKER -> {
                            attachments.add(
                                StickerModel(
                                    attachment.sticker.images[3].url,
                                    attachment.sticker.images_with_background[3].url
                                )
                            )
                        }
                        ATTACHMENTS_TYPE_PHOTO -> {
                            val photo = attachment.photo.sizes.maxBy { sizes -> sizes.width }
                            if (photo != null) {
                                attachments.add(PhotoModel(url = photo.url))
                            }
                        }
                    }
                }
            }

            threadComments = ArrayList()
            threadCommentAttachments = ArrayList()

            if (item.thread.count > 0) {
                for (threadItem in item.thread.items) {
                    for (source in sourceProfiles) {
                        if (source.id == threadItem.from_id) {
                            threadCommentName = source.name
                            threadCommentAvatar = source.avatar
                        }
                    }
                    if (!threadItem.attachments.isNullOrEmpty()) {
                        for (attachment in threadItem.attachments) {
                            when (attachment.type) {
                                ATTACHMENTS_TYPE_STICKER -> {
                                    threadCommentAttachments.add(
                                        StickerModel(
                                            attachment.sticker.images[3].url,
                                            attachment.sticker.images_with_background[3].url
                                        )
                                    )
                                }
                                ATTACHMENTS_TYPE_PHOTO -> {
                                    val photo = attachment.photo.sizes.maxBy { sizes -> sizes.width }
                                    if (photo != null) {
                                        threadCommentAttachments.add(PhotoModel(url = photo.url))
                                    }
                                }
                            }
                        }
                    }

                    isFavorite = threadItem.likes.user_likes != 0

                    threadComments.add(
                        CommentModel(
                            name = threadCommentName,
                            avatar = threadCommentAvatar,
                            date = threadItem.date,
                            text = threadItem.text,
                            attachments = threadCommentAttachments,
                            thread = ArrayList(),
                            likesCount = threadItem.likes.count,
                            userLikes = threadItem.likes.user_likes,
                            ownerId = threadItem.owner_id,
                            itemId = threadItem.id,
                            isFavorite = isFavorite
                        )
                    )
                }
            }

            isFavorite = item.likes.user_likes != 0

            commentsList.add(
                CommentModel(
                    name = name,
                    avatar = avatar,
                    date = date,
                    text = text,
                    attachments = attachments,
                    thread = threadComments,
                    likesCount = item.likes.count,
                    userLikes = item.likes.user_likes,
                    ownerId = item.owner_id,
                    itemId = item.id,
                    isFavorite = isFavorite
                )
            )
        }

        return commentsList
    }

}
