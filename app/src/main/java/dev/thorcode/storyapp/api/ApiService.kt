package dev.thorcode.storyapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body info: RegisterUserReq
    ): RegisterUserRes

    @POST("login")
    suspend fun login(
        @Body info: LoginUserReq
    ): LoginUserRes

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
    ): AllStoriesRes

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String,
        @Header("Authorization") token: String,
    ): DetailStory

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    )
}