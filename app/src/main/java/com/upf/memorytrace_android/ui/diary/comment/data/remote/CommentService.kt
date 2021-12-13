package com.upf.memorytrace_android.ui.diary.comment.data.remote

import com.upf.memorytrace_android.api.model.BaseResponse
import retrofit2.http.*

interface CommentService {

    @GET("/comment/list/{did}")
    suspend fun fetCommentList(@Path("did") diaryId: Int): BaseResponse<List<CommentsResponse>>

    @POST("/comment")
    suspend fun postComment(@Body postCommentRequest: PostCommentRequest): BaseResponse<PostCommentResponse>

    @PUT("/comment/{cid}")
    suspend fun deleteComment(@Path("cid") commentId: Int): BaseResponse<Nothing>
}