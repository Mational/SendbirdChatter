package com.mational.sendbirdchatter.domain.model

import com.google.gson.annotations.SerializedName

data class SendBirdPushNotification(
    val message: String,
    @SerializedName("push_title") val pushTitle: String,
    val channel: Channel
)

data class Channel(
    @SerializedName("channel_url") val channelUrl: String
)
