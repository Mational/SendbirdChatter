package com.mational.sendbirdchatter.data.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.mational.sendbirdchatter.R
import com.mational.sendbirdchatter.data.model.RegistrationToken
import com.mational.sendbirdchatter.domain.model.SendBirdPushNotification
import com.mational.sendbirdchatter.domain.repository.SendBirdRepository
import com.mational.sendbirdchatter.presentation.MainActivity
import com.sendbird.android.SendbirdChat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONException
import javax.inject.Inject
import kotlin.coroutines.resume

@AndroidEntryPoint
class MyFirebaseMessagingService: FirebaseMessagingService() {
    @Inject
    lateinit var registrationToken: RegistrationToken
    @Inject
    lateinit var sendBirdRepository: SendBirdRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private suspend fun connectUser(userId: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            SendbirdChat.connect(userId) { _, e ->
                if (e == null) {
                    cont.resume(Result.success(Unit))
                } else {
                    cont.resume(Result.failure(e))
                }
            }
        }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        registrationToken.storeToken(token)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userId = user.uid
            serviceScope.launch {
                val connectionResult = connectUser(userId)
                connectionResult.onSuccess {
                    val registrationResult = sendBirdRepository.registerPushToken(userId, token)
                    registrationResult.onSuccess {
                    }
                }
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.containsKey("sendbird")) {
            try {
                val sendbirdJson = remoteMessage.data["sendbird"]
                val pushNotification =
                    Gson().fromJson(sendbirdJson, SendBirdPushNotification::class.java)

                sendNotification(
                    applicationContext,
                    pushNotification.pushTitle,
                    pushNotification.message,
                    pushNotification.channel.channelUrl
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            super.onMessageReceived(remoteMessage)
        }
    }

    private fun sendNotification(
        context: Context,
        pushTitle: String,
        message: String,
        channelUrl: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("channelUrl", channelUrl)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Implement your own way to create and show a notification containing the received FCM message.
        val notificationBuilder = NotificationCompat.Builder(context, channelUrl)
            .setSmallIcon(R.drawable.logo_sendbird)
            .setColor(Color.parseColor("#7469C4"))
            .setContentTitle(pushTitle)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Utwórz NotificationChannel, jeśli jest to wymagane (Android O i nowsze)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "SendbirdChatter Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelUrl, channelName, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = notificationBuilder.build()
        notificationManager.notify(0, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel() // Anuluj wszystkie operacje przy zamknięciu serwisu
    }
}