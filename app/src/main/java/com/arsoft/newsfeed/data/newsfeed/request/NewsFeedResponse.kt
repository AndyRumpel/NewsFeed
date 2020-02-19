package com.arsoft.newsfeed.data.newsfeed.request



data class NewsFeedResponse (
    val response: Response
)

data class Attachment(
    val type: String,
    val photo: Photo,
    val video: Video,
    val doc: Doc
)

data class Doc(
    val id: Int,
    val owner_id: Int,
    val title: String,
    val sizes: Int,
    val ext: String,
    val url: String,
    val date: Long,
    val type: Int,
    val preview: Preview,
    val is_licensed: Int,
    val access_key: String
)

data class Preview(
    val photo: PreviewPhoto,
    val video: PreviewVideo
)

data class PreviewPhoto(
    val sizes: List<Sizes>
)

data class PreviewVideo(
    val src: String,
    val width: Int,
    val height: Int,
    val file_size: Int
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
    val is_favorite : Int,
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
    val can_post : Int,
    val groups_can_post: Boolean
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
    var attachments : List<Attachment>,
    val post_source : Post_source,
    val comments : Comments,
    val likes : Likes,
    val reposts : Reposts,
    val views : Views?,
    val is_favorite : Boolean,
    val post_id : Long,
    val push_subscription : Push_subscription,
    val track_code : String,
    val copy_history: List<Copy_history>
)

data class Copy_history (
    val id : Int,
    val owner_id : Int,
    val from_id : Int,
    val date : Int,
    val post_type : String,
    val text : String,
    val attachments : List<Attachment>,
    val post_source : Post_source
)

data class Likes (

    var count : Int,
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

    val count : Int?
)