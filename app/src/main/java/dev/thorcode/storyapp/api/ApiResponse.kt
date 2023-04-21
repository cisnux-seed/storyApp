package dev.thorcode.storyapp.api

import com.google.gson.annotations.SerializedName

data class RegisterUserReq(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)

data class RegisterUserRes(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class LoginUserReq(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)

data class LoginUserRes(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult
)

data class LoginResult(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)