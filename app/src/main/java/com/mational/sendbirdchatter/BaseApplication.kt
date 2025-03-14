package com.mational.sendbirdchatter

import android.app.Application
import com.sendbird.uikit.compose.SendbirdUikitCompose
import com.sendbird.uikit.core.data.model.UikitInitParams
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn

const val AppId = BuildConfig.APPLICATION_ID

@HiltAndroidApp
class BaseApplication : Application() {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        SendbirdUikitCompose.init(
            UikitInitParams(
                appId = AppId,
                context = this,
                isForeground = true
            )
        ).launchIn(appScope)
    }

    override fun onTerminate() {
        super.onTerminate()
        appScope.cancel()
    }
}