package com.one.fruitmanseller.utils.fcm

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FirebaseServ"

    @SuppressLint("BinaryOperationInTimber")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null) {
            Log.d(TAG, "notifi: " + remoteMessage.from )
            val i : MutableMap<String, String> = remoteMessage.data
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            NotificationHandler.displayNotification(applicationContext, title!!, body!!, i)
        }

        Log.d(TAG, "From: " + remoteMessage.from )
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }


}