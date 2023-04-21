package dev.thorcode.storyapp.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body info: RegisterUserReq
    ): RegisterUserRes

    @POST("login")
    suspend fun login(
        @Body info: LoginUserReq
    ): LoginUserRes
}