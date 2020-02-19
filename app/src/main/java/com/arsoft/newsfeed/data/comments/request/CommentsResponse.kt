package com.arsoft.newsfeed.data.comments.request

import com.arsoft.newsfeed.data.newsfeed.request.Photo
import com.arsoft.newsfeed.data.newsfeed.request.Video

data class CommentsResponse (

    val response : Response
)

data class Response (

    val count : Int,
    val items : List<Items>,
    val profiles : List<Profiles>,
    val groups : List<Groups>,
    val current_level_count : Int,
    val can_post : Boolean,
    val show_reply_button : Boolean,
    val groups_can_post : Boolean
)

data class Items (

    val id : Int,
    val from_id : Int,
    val post_id : Int,
    val owner_id : Long,
    val parents_stack : List<String>,
    val date : Long,
    val text : String,
    val thread : Thread,
    val attachments: ArrayList<Attachment>
)

data class Attachment(
    val type: String,
    val sticker: Sticker,
    val photo: Photo,
    val video: Video
)

data class Sticker(
    val product_id: Int,
    val sticker_id: Int,
    val images: ArrayList<StickerImage>,
    val images_with_background: ArrayList<StickerImage>,
    val animation_url: String
)

data class StickerImage(
    val url: String,
    val width: Int,
    val height: Int
)

data class Thread (

    val count : Int,
    val items : List<String>,
    val can_post : Boolean,
    val show_reply_button : Boolean,
    val groups_can_post : Boolean
)

data class Profiles (

    val id : Long,
    val first_name : String,
    val last_name : String,
    val is_closed : Boolean,
    val can_access_closed : Boolean,
    val sex : Int,
    val screen_name : String,
    val photo_50 : String,
    val photo_100 : String,
    val online : Int,
    val online_info : Online_info
)

data class Groups (

    val id : Long,
    val name : String,
    val screen_name : String,
    val is_closed : Int,
    val type : String,
    val is_admin : Int,
    val is_member : Int,
    val is_advertiser : Int,
    val photo_50 : String,
    val photo_100 : String,
    val photo_200 : String
)


data class Online_info (

    val visible : Boolean,
    val last_seen : Int
)