package com.arsoft.newsfeed.data.comments.request

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface CommentsService {

    @GET(value = "wall.getComments")
    fun getComments(
        @Query(value = "owner_id") ownerId: Long,
        @Query(value = "post_id") post_id: Long,
        @Query(value = "access_token") accessToken: String,
        @Query(value = "v") version: String,
        @Query(value = "extended") extended: Int,
        @Query(value = "need_likes") needLikes: Int,
        @Query(value = "thread_items_count") threadItemsCount: Int,
        @Query(value = "count") count: Int
    ): Deferred<CommentsResponse>

    @GET(value = "wall.createComment")
    fun createComment(
        @Query(value = "owner_id") ownerId: Long,
        @Query(value = "post_id") postId: Long,
        @Query(value = "access_token") accessToken: String,
        @Query(value = "message") message: String,
        @Query(value = "v") version: String
    ): Deferred<CreateCommentResponse>

    @GET(value = "wall.createComment")
    fun replyComment(
        @Query(value = "owner_id") ownerId: Long,
        @Query(value = "post_id") postId: Long,
        @Query(value = "access_token") accessToken: String,
        @Query(value = "reply_to_comment") replyToComment: Long,
        @Query(value = "message") message: String,
        @Query(value = "v") version: String
    ): Deferred<CreateCommentResponse>

}