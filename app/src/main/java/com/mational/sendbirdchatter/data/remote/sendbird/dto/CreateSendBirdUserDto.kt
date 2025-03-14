package com.mational.sendbirdchatter.data.remote.sendbird.dto


import com.google.gson.annotations.SerializedName

data class CreateSendBirdUserDto(
    @SerializedName("user_id") val userId: String,
    val nickname: String,
    @SerializedName("profile_url") val profileUrl: String = "https://sendbird.com/main/img/profiles/profile_05_512px.png",
    @SerializedName("issue_access_token") val issueAccessToken: Boolean = true
)