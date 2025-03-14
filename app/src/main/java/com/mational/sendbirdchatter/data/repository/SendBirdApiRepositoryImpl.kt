package com.mational.sendbirdchatter.data.repository

import com.mational.sendbirdchatter.data.remote.sendbird.api.SendBirdApiService
import com.mational.sendbirdchatter.data.remote.sendbird.dto.CreateSendBirdUserDto
import com.mational.sendbirdchatter.data.remote.sendbird.dto.GetAllOpenChannelsDto
import com.mational.sendbirdchatter.data.remote.sendbird.dto.RegisterUserFcmTokenDto
import com.mational.sendbirdchatter.data.remote.sendbird.dto.SendBirdUserDto
import com.mational.sendbirdchatter.domain.repository.SendBirdApiRepository
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result

@Singleton
class SendBirdApiRepositoryImpl @Inject constructor(
    private val sendBirdApiService: SendBirdApiService
): SendBirdApiRepository {
    private suspend fun <T : Any> callApi(
        requestCall: suspend () -> Response<T>
    ): Result<T> {
        return try {
            val response = requestCall()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Result.success(body)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception("Error ${response.code()}: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAllOpenChannels(): Result<GetAllOpenChannelsDto> {
        return callApi { sendBirdApiService.getAllOpenChannels() }
    }

    override suspend fun createUser(userId: String, nickname: String): Result<SendBirdUserDto> {
        val newUser = CreateSendBirdUserDto(
            userId = userId,
            nickname = nickname
        )
        return callApi { sendBirdApiService.createUser(newUser) }
    }

    override suspend fun getUser(userId: String): Result<SendBirdUserDto> {
        return callApi { sendBirdApiService.getUser(userId) }
    }

    override suspend fun registerUserFCMToken(userId: String, token: String): Result<Unit> {
        val body = RegisterUserFcmTokenDto(gcmRegToken = token)
        return callApi { sendBirdApiService.registerUserFCMToken(userId, body) }
    }
}
