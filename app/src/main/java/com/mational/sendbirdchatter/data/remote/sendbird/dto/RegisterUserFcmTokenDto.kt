package com.mational.sendbirdchatter.data.remote.sendbird.dto

import com.google.gson.annotations.SerializedName

data class RegisterUserFcmTokenDto(
    @SerializedName("gcm_reg_token") val gcmRegToken: String
)