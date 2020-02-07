package com.arsoft.newsfeed.data.newsfeed.repository

import android.provider.ContactsContract
import android.util.Log
import com.arsoft.newsfeed.helpers.MyDateTimeFormatHelper
import com.arsoft.newsfeed.data.models.*
import com.arsoft.newsfeed.data.newsfeed.request.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class NewsFeedRepository(val apiService: NewsFeedService) {


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
//        var postText = ""

        var videoPreviewImage = ""
        var videoDuration = 0
        var videoID = ""
        var videoOwnerID = ""
        var videoPreviewImageWidth = 0
//
//        val currentTime = Calendar.getInstance().timeInMillis
//        var attachments = ArrayList<IAttachment>()
        val photoSizes = ArrayList<Sizes>()


        var photoURLs: String? = null
        val currentTime = Calendar.getInstance().timeInMillis
        var attachments = ArrayList<IAttachment>()
        var postText = ""


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

//        for (item in result.response.items) {
//            for (source in sourceProfiles) {
//                if (abs(item.source_id) == source.id) {
//                    if (!item.attachments.isNullOrEmpty()) {
//
//                        videoPreviewImage = ""
//                        videoDuration = 0
//                        videoID = ""
//                        videoOwnerID = ""
//                        videoPreviewImageWidth = 0
//                        photoURLs = null
//                        attachments = ArrayList()
//                        avatar = source.avatar
//                        name = source.name
//
//                        for (attachment in item.attachments) {
//                            when (attachment.type) {
//                                ATTACHMENTS_TYPE_PHOTO -> {
//                                    for (size in attachment.photo.sizes) {
//                                        if (size.type == PHOTO_SIZE_WHERE_MAX_SIDE_604PX) {
//                                            photoURLs = size.url
//                                        }
//                                    }
//                                    attachments.add(PhotoModel(photoURLs!!))
//                                }
//                                ATTACHMENTS_TYPE_VIDEO -> {
//                                    attachment.video.image.forEach {
//                                        if (it.width in (videoPreviewImageWidth + 1)..999) {
//                                            videoPreviewImageWidth = it.width
//                                        }
//                                    }
//
//                                    for (image in attachment.video.image) {
//
//                                        if (videoPreviewImageWidth == image.width  ) {
//                                            videoPreviewImage = image.url
//                                            videoDuration = attachment.video.duration
//                                            videoOwnerID = attachment.video.owner_id.toString()
//                                            videoID = "${videoOwnerID}_${attachment.video.id}"
//                                        }
//                                    }
//                                    if (
//                                        videoPreviewImage != null &&
//                                        videoDuration != null &&
//                                        videoID != null &&
//                                        videoOwnerID != null)
//                                    {
//                                        attachments.add(VideoModel(
//                                            videoPreviewImage = videoPreviewImage,
//                                            videoDuration = videoDuration,
//                                            videoID = videoID,
//                                            videoOwnerID = videoOwnerID
//                                        ))
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            newsFeedList.add(
//                FeedItemModel(
//                    avatar = avatar,
//                    sourceName = name,
//                    postText = item.text,
//                    attachments = attachments,
//                    date = MyDateTimeFormatHelper.timeFormat(item.date * 1000, currentTime)))
//        }


        for (item in result.response.items) {
            for (source in sourceProfiles) {

                if (abs(item.source_id) == source.id) {

                    avatar = source.avatar
                    name = source.name
                    postText = item.text

                    attachments.clear()

                    if (!item.attachments.isNullOrEmpty()) {
                        for (attachment in item.attachments) {
                            when(attachment.type) {
                                ATTACHMENTS_TYPE_PHOTO -> {
//                                    val photoWithMaxWidth = attachment.photo.sizes.maxBy { sizes -> sizes.width }
//                                    if (photoWithMaxWidth != null) {
//                                        attachments.add(PhotoModel(photoURL = photoWithMaxWidth.url))
//                                    }

                                    for (size in attachment.photo.sizes) {
                                        if (size.type == PHOTO_SIZE_WHERE_MAX_SIDE_604PX) {
                                            photoURLs = size.url
                                        }
                                    }
                                    attachments.add(PhotoModel(photoURLs!!))

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

            newsFeedList.add(
                FeedItemModel(
                    avatar = avatar,
                    sourceName = name,
                    postText = postText,
                    date = MyDateTimeFormatHelper.timeFormat(postDate = item.date * 1000, currentTime = currentTime),
                    attachments = attachments
                ))
        }

        newsFeedList.forEach {
            Log.e("ATTACHMNETS", "\n \n \n$it\n \n \n----------------")
        }

        return newsFeedList
    }
}