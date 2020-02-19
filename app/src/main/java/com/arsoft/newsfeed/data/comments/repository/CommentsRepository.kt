package com.arsoft.newsfeed.data.comments.repository

import com.arsoft.newsfeed.data.comments.request.Attachment
import com.arsoft.newsfeed.data.comments.request.CommentsResponse
import com.arsoft.newsfeed.data.comments.request.CommentsService
import com.arsoft.newsfeed.data.models.*
import kotlin.math.abs

class CommentsRepository(private val apiService: CommentsService) {

    private val VERSION = "5.103"
    private val EXTENDED_RESPONSE = 1

    private val ATTACHMENTS_TYPE_PHOTO = "photo"
    private val ATTACHMENTS_TYPE_VIDEO = "video"
    private val ATTACHMENTS_TYPE_STICKER = "sticker"

    suspend fun loadComments(ownerId: Long, postId: Long, accessToken: String): ArrayList<CommentModel>{
        val result = apiService.getComments(
            ownerId = ownerId,
            accessToken = accessToken,
            post_id = postId,
            version = VERSION,
            extended = EXTENDED_RESPONSE
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
                if(abs(item.owner_id) == source.id) {
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

            commentsList.add(
                CommentModel(
                    name = name,
                    avatar = avatar,
                    date = date,
                    text = text,
                    attachments = attachments
                )
            )
        }


        return commentsList
    }

}
