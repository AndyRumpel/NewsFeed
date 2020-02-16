package com.arsoft.newsfeed.data.comments.request

import com.arsoft.newsfeed.data.models.IAttachment

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
    val stiker: Stiker,
    val photo: Photo
)

data class Thread (

    val count : Int,
    val items : List<String>,
    val can_post : Boolean,
    val show_reply_button : Boolean,
    val groups_can_post : Boolean
)