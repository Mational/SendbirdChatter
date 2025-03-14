package com.mational.sendbirdchatter.di

import com.mational.sendbirdchatter.BuildConfig
import com.mational.sendbirdchatter.data.remote.sendbird.api.SendBirdApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SendBirdNetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Api-Token", BuildConfig.SENDBIRD_API_TOKEN)
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        val appId = BuildConfig.APPLICATION_ID
        return Retrofit.Builder()
            .baseUrl("https://api-$appId.sendbird.com/v3/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSendBirdApiService(retrofit: Retrofit): SendBirdApiService {
        return retrofit.create(SendBirdApiService::class.java)
    }
}