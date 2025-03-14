package com.mational.sendbirdchatter.data.remote.sendbird.dto


import com.google.gson.annotations.SerializedName

data class GetAllOpenChannelsDto(
    val channels: List<Channel>,
    val next: String,
    val ts: Long
) {
    data class Channel(
        val name: String,
        @SerializedName("channel_url") val channelUrl: String,
        @SerializedName("custom_type") val customType: String,
        @SerializedName("is_ephemeral") val isEphemeral: Boolean,
        @SerializedName("participant_count") val participantCount: Int,
        @SerializedName("max_length_message") val maxLengthMessage: Int,
        @SerializedName("created_at") val createdAt: Int,
        @SerializedName("cover_url") val coverUrl: String,
        val `data`: String,
        val operators: List<Operator>,
        val freeze: Boolean,
        val metadata: Metadata
    ) {
        data class Operator(
            @SerializedName("user_id") val userId: String,
            val nickname: String,
            @SerializedName("profile_url") val profileUrl: String
        )

        data class Metadata(
            @SerializedName("background_image") val backgroundImage: String,
            @SerializedName("text_size") val textSize: String
        )
    }
}