package com.mational.sendbirdchatter.data.repository

import com.mational.sendbirdchatter.data.remote.sendbird.dto.SendBirdUserDto
import com.mational.sendbirdchatter.domain.repository.SendBirdApiRepository
import com.mational.sendbirdchatter.domain.repository.SendBirdRepository
import com.sendbird.uikit.compose.SendbirdUikitCompose
import com.sendbird.uikit.core.data.model.UikitCurrentUserInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SendBirdRepositoryImpl @Inject constructor(
    private val sendBirdApiRepository: SendBirdApiRepository
): SendBirdRepository {
    override fun prepareSendBird(sendBirdUser: SendBirdUserDto) {
        val userId = sendBirdUser.userId
        val authToken = sendBirdUser.accessToken
        SendbirdUikitCompose.prepare(
            UikitCurrentUserInfo(
                userId = userId,
                authToken = authToken
            )
        )
    }

    override suspend fun createSendBirdUser(userId: String, email: String): Result<SendBirdUserDto> {
        val createUserResult = sendBirdApiRepository.createUser(userId, email)
        return createUserResult.onSuccess { sendBirdUser ->
            Result.success(sendBirdUser)
        }.onFailure { exception ->
            Result.failure<Exception>(Exception("Create sendbird user failed", exception))
        }
    }

    override suspend fun getSendBirdUser(userId: String, email: String): Result<SendBirdUserDto> {
        val getUserResult = sendBirdApiRepository.getUser(userId)
        return getUserResult.onSuccess { sendBirdUser ->
            Result.success(sendBirdUser)
        }.onFailure { exception ->
            Result.failure<Exception>(Exception("Get sendbird user failed", exception))
        }
    }

    override suspend fun registerPushToken(userId: String, token: String): Result<Unit> {
        val result = sendBirdApiRepository.registerUserFCMToken(userId, token)
        return result.onSuccess {
            Result.success(Unit)
        }.onFailure { exception ->
            Result.failure<Exception>(Exception("Register push token failed", exception))
        }
    }
}
