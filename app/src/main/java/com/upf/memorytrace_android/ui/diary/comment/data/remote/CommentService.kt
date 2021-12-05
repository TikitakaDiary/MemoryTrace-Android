package com.upf.memorytrace_android.ui.diary.comment.data.remote

import com.upf.memorytrace_android.api.model.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {

    @GET("/comment/list/{did}")
    suspend fun fetCommentList(@Path("did") diaryId: Int): BaseResponse<List<CommentsResponse>>

    @POST("/comment")
    suspend fun postComment(@Body postCommentRequest: PostCommentRequest): BaseResponse<PostCommentResponse>
}