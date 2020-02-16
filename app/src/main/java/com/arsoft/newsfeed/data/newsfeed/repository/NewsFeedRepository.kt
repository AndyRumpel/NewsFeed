package com.arsoft.newsfeed.data.newsfeed.repository

import android.util.Log
import com.arsoft.newsfeed.data.models.*
import com.arsoft.newsfeed.data.newsfeed.request.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class NewsFeedRepository(private val apiService: NewsFeedService) {


    private val ITEMS_COUNT = 100
    private val VERSION = "5.103"
    private val FILTERS = "post"

    private val ATTACHMENTS_TYPE_PHOTO = "photo"
    private val ATTACHMENTS_TYPE_VIDEO = "video"
    private val ATTACHMENTS_TYPE_DOC = "doc"
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

        return parseData(result)
    }

    suspend fun loadMoreNewsFeed(accessToken: String, startFrom: String): ArrayList<FeedItemModel> {
        val result = apiService.loadMoreNewsFeed(
            count = ITEMS_COUNT,
            accessToken = accessToken,
            version = VERSION,
            filters = FILTERS,
            startFrom = startFrom
        ).await()

        return parseData(result)
    }

    fun parseData(result: NewsFeedResponse): ArrayList<FeedItemModel> {
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
        var startFrom = result.response.next_from

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
                likes = com.arsoft.newsfeed.data.newsfeed.request.Likes(
                    count = count,
                    can_like = can_like,
                    user_likes = user_likes,
                    can_publish = can_publish
                )
            }

            with(item.comments) {
                comments = com.arsoft.newsfeed.data.newsfeed.request.Comments(
                    count = count,
                    can_post = can_post,
                    groups_can_post = groups_can_post
                )
            }

            with(item.reposts) {
                reposts = com.arsoft.newsfeed.data.newsfeed.request.Reposts(
                    count = count,
                    user_reposted = user_reposted
                )
            }

            with(item.views) {
                if (count == null) {
                    views = com.arsoft.newsfeed.data.newsfeed.request.Views(
                        count = 0
                    )
                } else {
                    views = com.arsoft.newsfeed.data.newsfeed.request.Views(
                        count = count
                    )
                }
            }

            postText = item.text


            for (source in sourceProfiles) {
                if (abs(item.source_id) == source.id) {
                    avatar = source.avatar
                    name = source.name
                }
            }

            if (!item.attachments.isNullOrEmpty()) {
                for (attachment in item.attachments) {
                    when(attachment.type) {
                        ATTACHMENTS_TYPE_PHOTO -> {
                            val photo = attachment.photo.sizes.maxBy { sizes -> sizes.width }
                            if (photo != null) {
                                attachments.add(PhotoModel(url = photo.url))
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
                        ATTACHMENTS_TYPE_DOC -> {
                            attachments.add(DocModel(url = attachment.doc.url))
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
                        isFavorite = isFavorite,
                        startFrom = startFrom
                    ))
            }
        }

        return newsFeedList
    }
}