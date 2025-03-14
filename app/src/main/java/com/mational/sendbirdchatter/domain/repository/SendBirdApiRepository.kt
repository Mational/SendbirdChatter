package com.mational.sendbirdchatter.domain.repository

import com.mational.sendbirdchatter.data.remote.sendbird.dto.GetAllOpenChannelsDto
import com.mational.sendbirdchatter.data.remote.sendbird.dto.SendBirdUserDto

interface SendBirdApiRepository {
    suspend fun getAllOpenChannels(): Result<GetAllOpenChannelsDto>

    suspend fun createUser(
        userId: String,
        nickname: String
    ): Result<SendBirdUserDto>

    suspend fun getUser(userId: String): Result<SendBirdUserDto>

    suspend fun registerUserFCMToken(
        userId: String,
        token: String
    ): Result<Unit>
}