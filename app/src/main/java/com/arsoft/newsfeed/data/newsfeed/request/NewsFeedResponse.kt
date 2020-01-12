package com.arsoft.newsfeed.data.newsfeed.request

data class NewsFeedResponse (
    val response: Response
)

data class Attachments (
    val type : String,
    val photo : Photo?,
    val video : Video?
)

data class Video (

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
    val access_access_key : String,
    val can_add : Int,
    val track_code : String,
    val can_comment : Int,
    val can_repost : Int,
    val can_like : Int,
    val can_add_to_faves : Int,
    val can_subscribe : Int,
    val type : String
)

data class Image (

    val height : Int,
    val url : String,
    val width : Int,
    val with_padding : Int
)

data class First_frame (

    val url : String,
    val width : Int,
    val height : Int
)

data class Comments (

    val count : Int,
    val can_post : Int
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

data class Items (

    val can_doubt_category : Boolean,
    val can_set_category : Boolean,
    val topic_id : Int,
    val type : String,
    val source_id : Long,
    val date : Long,
    val post_type : String,
    val text : String,
    val marked_as_ads : Int,
    val attachments : List<Attachments>,
    val post_source : Post_source,
    val comments : Comments,
    val likes : Likes,
    val reposts : Reposts,
    val views : Views,
    val is_favorite : Boolean,
    val post_id : Int,
    val push_subscription : Push_subscription,
    val track_code : String
)

data class Likes (

    val count : Int,
    val user_likes : Int,
    val can_like : Int,
    val can_publish : Int
)

data class Online_info (

    val visible : Boolean,
    val is_online : Boolean,
    val is_mobile : Boolean
)

data class Photo (

    val id : Long,
    val album_id : Int,
    val owner_id : Int,
    val user_id : Int,
    val sizes : List<Sizes>,
    val text : String,
    val date : Int,
    val post_id : Int,
    val access_access_key : String
)

data class Post_source (

    val type : String
)

data class Profiles (

    val id : Long,
    val first_name : String,
    val last_name : String,
    val is_closed : Boolean,
    val can_access_closed : Boolean,
    val is_service : Boolean,
    val sex : Int,
    val screen_name : String,
    val photo_50 : String,
    val photo_100 : String,
    val online : Int,
    val online_mobile : Int,
    val online_info : Online_info
)

data class Push_subscription (

    val is_subscribed : Boolean
)

data class Reposts (

    val count : Int,
    val user_reposted : Int
)

data class Response (

    val items : List<Items>,
    val profiles : List<Profiles>,
    val groups : List<Groups>,
    val next_from : String
)

data class Sizes (

    val type : Char,
    val url : String,
    val width : Int,
    val height : Int
)

data class Views (

    val count : Int
)