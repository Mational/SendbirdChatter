package com.mational.sendbirdchatter.data.remote.sendbird.dto


import com.google.gson.annotations.SerializedName

data class SendBirdUserDto(
    @SerializedName("user_id") val userId: String,
    val nickname: String,
    @SerializedName("profile_url") val profileUrl: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("is_online") val isOnline: Boolean,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("created_at") val createdAt: Int,
    @SerializedName("last_seen_at") val lastSeenAt: Long,
    @SerializedName("discovery_keys") val discoveryKeys: List<String>,
    @SerializedName("preferred_languages") val preferredLanguages: List<String>,
    @SerializedName("has_ever_logged_in") val hasEverLoggedIn: Boolean,
    val metadata: Metadata,
    @SerializedName("unread_channel_count") val unreadChannelCount: Int,
    @SerializedName("unread_message_count") val unreadMessageCount: Int
) {
    data class Metadata(
        @SerializedName("font_preference") val fontPreference: String,
        @SerializedName("font_color") val fontColor: String
    )
}