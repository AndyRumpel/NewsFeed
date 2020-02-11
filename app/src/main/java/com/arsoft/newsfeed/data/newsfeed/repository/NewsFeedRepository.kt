package com.arsoft.newsfeed.data.newsfeed.repository

import android.provider.ContactsContract
import android.util.Log
import com.arsoft.newsfeed.helpers.MyDateTimeFormatHelper
import com.arsoft.newsfeed.data.models.*
import com.arsoft.newsfeed.data.newsfeed.request.*
import com.github.chrisbanes.photoview.PhotoView
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class NewsFeedRepository(private val apiService: NewsFeedService) {


    private val ITEMS_COUNT = 100
    private val VERSION = "5.103"
    private val FILTERS = "post"

    private val ATTACHMENTS_TYPE_PHOTO = "photo"
    private val ATTACHMENTS_TYPE_VIDEO = "video"
    private val PHOTO_SIZE_WHERE_MAX_SIDE_807PX = 'y'
    private val PHOTO_SIZE_WHERE_MAX_SIDE_604PX = 'x'
    private val VIDEO_PREVIEW_IMAGE_WIDTH = 720

    private val sourceProfiles = ArrayList<SourceModel>()
    private val newsFeedList = ArrayList<FeedItemModel>()

    suspend fun getNewsFeed(accessToken: String): ArrayList<FeedItemModel> {
        val result = apiService.getNewsFeed(
            count = ITEMS_COUNT,
            accessToken = accessToken,
            version = VERSION,
            filters = FILTERS
        ).await()

        var avatar = ""
        var name = ""

        var videoPreviewImage = ""
        var videoDuration = 0
        var videoID = ""
        var videoOwnerID = ""
        var videoPreviewImageWidth = 0
        var attachments: ArrayList<IAttachment>
        var postText = ""
        var likes: Likes
        var comments: Comments
        var reposts: Reposts
        var views: Views
        var ownerId = 0L
        var postId = 0L
        var isFavorite: Boolean
        var photoWidth = 0

        var isValid: Boolean


        for (item in result.response.profiles) {
            sourceProfiles.add(
                SourceModel(
                    id = item.id,
                    name = "${item.first_name} ${item.last_name}",
                    avatar = item.photo_100)
            )
        }

        for (item in result.response.groups) {
            sourceProfiles.add(
                SourceModel(
                    id = item.id,
                    name = item.name,
                    avatar = item.photo_100))
        }

        for (item in result.response.items) {

            attachments = ArrayList()
            isValid = true
            ownerId = item.source_id
            postId = item.post_id
            isFavorite = item.is_favorite

            with(item.likes) {
                likes = Likes(
                    count = count,
                    can_like = can_like,
                    user_likes = user_likes,
                    can_publish = can_publish
                )
            }

            with(item.comments) {
                comments = Comments(
                    count = count,
                    can_post = can_post,
                    groups_can_post = groups_can_post
                )
            }

            with(item.reposts) {
                reposts = Reposts(
                    count = count,
                    user_reposted = user_reposted
                )
            }

            with(item.views) {
                views = Views(
                    count = count
                )
            }


            for (source in sourceProfiles) {
                if (abs(item.source_id) == source.id) {

                    avatar = source.avatar
                    name = source.name
                    if (!item.copy_history.isNullOrEmpty()) {
                        isValid = false
                    }

                    if (item.text.isBlank()) {
                        postText = ""
                    } else {
                        postText = item.text
                    }

                    if (!item.attachments.isNullOrEmpty()) {
                        for (attachment in item.attachments) {
                            when(attachment.type) {
                                ATTACHMENTS_TYPE_PHOTO -> {
//                                    attachment.photo.sizes.forEach{
//                                        if  (it.width in (photoWidth)..999) {
//                                            photoWidth = it.width
//                                        }
//                                    }
//
//                                    for (photo in attachment.photo.sizes) {
//                                        if (photo.width == photoWidth) {
//                                            attachments.add(PhotoModel(photoURL = photo.url))
//                                        }
//                                    }

                                    val photo = attachment.photo.sizes.maxBy { sizes -> sizes.width }
                                    if (photo != null) {
                                        attachments.add(PhotoModel(photoURL = photo.url))
                                    }
                                }
                                ATTACHMENTS_TYPE_VIDEO -> {
                                    attachment.video.image.forEach {
                                        if (it.width in (videoPreviewImageWidth + 1)..999) {
                                            videoPreviewImageWidth = it.width
                                        }
                                    }

                                    for (image in attachment.video.image) {
                                        if (videoPreviewImageWidth == image.width) {
                                            videoPreviewImage = image.url
                                            videoDuration = attachment.video.duration
                                            videoOwnerID = attachment.video.owner_id.toString()
                                            videoID = "${videoOwnerID}_${attachment.video.id}"

                                        }
                                    }

                                    if (
                                        videoPreviewImage != "" &&
                                        videoDuration != 0 &&
                                        videoID != "" &&
                                        videoOwnerID != ""){

                                        attachments.add(VideoModel(
                                            videoPreviewImage = videoPreviewImage,
                                            videoDuration = videoDuration,
                                            videoOwnerID = videoOwnerID,
                                            videoID = videoID
                                        ))

                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (isValid) {
                newsFeedList.add(
                    FeedItemModel(
                        avatar = avatar,
                        sourceName = name,
                        postText = postText,
                        attachments = attachments,
                        date = item.date,
                        likes = likes,
                        comments = comments,
                        reposts = reposts,
                        views = views,
                        ownerId = ownerId,
                        postId = postId,
                        isFavorite = isFavorite
                    ))
            }
        }

        return newsFeedList
    }
}