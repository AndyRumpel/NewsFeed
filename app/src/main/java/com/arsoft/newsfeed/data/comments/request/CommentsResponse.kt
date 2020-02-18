package com.arsoft.newsfeed.data.comments.request

import com.arsoft.newsfeed.data.models.IAttachment
import com.arsoft.newsfeed.data.newsfeed.request.Photo

data class CommentsResponse (

    val response : Response
)

data class Response (

    val count : Int,
    val items : List<Items>,
    val current_level_count : Int,
    val can_post : Boolean,
    val show_reply_button : Boolean,
    val groups_can_post : Boolean
)

data class Items (

    val id : Int,
    val from_id : Int,
    val post_id : Int,
    val owner_id : Int,
    val parents_stack : List<String>,
    val date : Int,
    val text : String,
    val thread : Thread,
    val attachments: ArrayList<Attachment>
)

data class Attachment(
    val type: String,
    val sticker: Sticker,
    val photo: Photo
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