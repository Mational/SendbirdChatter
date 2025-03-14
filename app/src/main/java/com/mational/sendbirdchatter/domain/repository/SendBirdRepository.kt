package com.mational.sendbirdchatter.domain.repository

import com.mational.sendbirdchatter.data.remote.sendbird.dto.SendBirdUserDto

interface SendBirdRepository {
    fun prepareSendBird(sendBirdUser: SendBirdUserDto)

    suspend fun createSendBirdUser(userId: String, email: String): Result<SendBirdUserDto>

    suspend fun getSendBirdUser(userId: String, email: String): Result<SendBirdUserDto>

    suspend fun registerPushToken(userId: String, token: String): Result<Unit>
}