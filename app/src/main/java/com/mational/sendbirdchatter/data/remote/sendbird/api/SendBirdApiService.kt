package com.mational.sendbirdchatter.data.remote.sendbird.api

import com.mational.sendbirdchatter.data.remote.sendbird.dto.CreateSendBirdUserDto
import com.mational.sendbirdchatter.data.remote.sendbird.dto.GetAllOpenChannelsDto
import com.mational.sendbirdchatter.data.remote.sendbird.dto.RegisterUserFcmTokenDto
import com.mational.sendbirdchatter.data.remote.sendbird.dto.SendBirdUserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SendBirdApiService {
    @GET("open_channels")
    suspend fun getAllOpenChannels(): Response<GetAllOpenChannelsDto>

    @POST("users")
    suspend fun createUser(
        @Body createSendBirdUserDto: CreateSendBirdUserDto
    ): Response<SendBirdUserDto>

    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: String
    ): Response<SendBirdUserDto>

    @POST("users/{userId}/push/gcm")
    suspend fun registerUserFCMToken(
        @Path("userId") userId: String,
        @Body body: RegisterUserFcmTokenDto
    ): Response<Unit>
}