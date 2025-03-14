package com.mational.sendbirdchatter.di

import com.mational.sendbirdchatter.data.repository.SendBirdApiRepositoryImpl
import com.mational.sendbirdchatter.domain.repository.SendBirdApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SendBirdApiRepositoryModule {
    @Binds
    abstract fun bindSendBirdApiRepository(
        impl: SendBirdApiRepositoryImpl
    ): SendBirdApiRepository
}