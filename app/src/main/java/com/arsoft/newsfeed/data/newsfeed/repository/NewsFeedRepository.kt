package com.arsoft.newsfeed.data.newsfeed.repository

import android.util.Log
import com.arsoft.newsfeed.MyDateTimeFormatHelper
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


        var photoURLs: String?
        var videoPreviewImage: String?
        var videoDuration: Int?
        var videoID: String?
        var videoOwnerID: String?
        var videoPreviewImageWidth: Int
        val currentTime = Calendar.getInstance().timeInMillis
        var attachments: ArrayList<IAttachment>
        var postText: String

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
            for (source in sourceProfiles) {
                if (abs(item.source_id) == source.id) {
                    if (!item.attachments.isNullOrEmpty()) {
                        videoPreviewImage = null
                        videoDuration = null
                        videoID = null
                        videoOwnerID = null
                        videoPreviewImageWidth = 0
                        photoURLs = null
                        postText = ""
                        attachments = ArrayList()

                        if (item.text != "") {
                            postText = item.text
                        }

                        for (attachment in item.attachments) {
                            if (attachment.type == ATTACHMENTS_TYPE_PHOTO) {
                                for (size in attachment.photo.sizes) {
                                    if (size.type == PHOTO_SIZE_WHERE_MAX_SIDE_604PX) {
                                        photoURLs = size.url
                                    }
                                }
                                attachments.add(PhotoModel(photoURLs!!))
                            }
                            if (attachment.type == ATTACHMENTS_TYPE_VIDEO) {

                                attachment.video.image.forEach {
                                    if (it.width in (videoPreviewImageWidth + 1)..999) {
                                        videoPreviewImageWidth = it.width
                                    }
                                }

                                for (image in attachment.video.image) {

                                    if (videoPreviewImageWidth == image.width  ) {
                                        videoPreviewImage = image.url
                                        videoDuration = attachment.video.duration
                                        videoOwnerID = attachment.video.owner_id.toString()
                                        videoID = "${videoOwnerID}_${attachment.video.id}"


//                                       Log.e("image_preview_width", videoPreviewImageWidth.toString())
//                                       Log.e("image_preview", videoPreviewImage.toString())
//                                       Log.e("VIDEO_DURATION", videoDuration.toString())

                                    }
                                    attachments.add(VideoModel(
                                        videoPreviewImage = videoPreviewImage,
                                        videoDuration = videoDuration,
                                        videoID = videoID,
                                        videoOwnerID = videoOwnerID
                                    ))

                                }
                            }
                        }

                        newsFeedList.add(
                            FeedItemModel(
                                avatar = source.avatar,
                                sourceName = source.name,
                                postText = postText,
                                attachments = attachments,
                                date = MyDateTimeFormatHelper.timeFormat(item.date * 1000, currentTime)))
//                        Log.e("image_preview_width", videoPreviewImageWidth.toString())
//                        Log.e("image_preview", videoPreviewImage.toString())
//                        Log.e("VIDEO_DURATION", videoDuration.toString())
//                        Log.e("POST_TEXT", source.name + "\n" + postText + "\n \n \n ------------------- \n \n \n")

                    }
                }
            }
        }


        return newsFeedList
    }
}