package com.mational.sendbirdchatter.di

import com.mational.sendbirdchatter.data.repository.SendBirdRepositoryImpl
import com.mational.sendbirdchatter.domain.repository.SendBirdRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SendBirdRepositoryModule {
    @Binds
    abstract fun bindSendBirdRepository(
        impl: SendBirdRepositoryImpl
    ): SendBirdRepository

}