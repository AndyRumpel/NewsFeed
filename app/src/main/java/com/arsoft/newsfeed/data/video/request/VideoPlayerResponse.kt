package com.arsoft.newsfeed.data.video.request

data class VideoPlayerResponse(
    val response: Response
)

data class Files (

    val mp4_240 : String,
    val mp4_360 : String,
    val mp4_480 : String,
    val mp4_720 : String,
    val hls : String
)

data class First_frame (

    val url : String,
    val width : Int,
    val height : Int
)

data class Image (

    val height : Int,
    val url : String,
    val width : Int,
    val with_padding : Int
)

data class Items (

    val id : Int,
    val owner_id : Int,
    val title : String,
    val duration : Int,
    val description : String,
    val date : Int,
    val comments : Int,
    val views : Int,
    val width : Int,
    val height : Int,
    val image : List<Image>,
    val is_favorite : Boolean,
    val first_frame : List<First_frame>,
    val files : Files,
    val timeline_thumbs : Timeline_thumbs,
    val player : String,
    val can_add : Int,
    val can_comment : Int,
    val can_repost : Int,
    val can_like : Int,
    val can_add_to_faves : Int,
    val can_subscribe : Int,
    val type : String
)

data class Response (

    val count : Int,
    val items : List<Items>
)

data class Timeline_thumbs (

    val frame_width : Int,
    val frame_height : Int,
    val count_per_row : Int,
    val count_per_image : Int,
    val count_total : Int,
    val links : List<String>
)