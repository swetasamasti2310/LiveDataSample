package com.example.swetakumari.livedatasample.Bubble

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Person
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.os.BuildCompat
import com.example.swetakumari.livedatasample.MyApplication
import com.example.swetakumari.livedatasample.R

object BubbleHelper {
    val application = MyApplication()
    val BUBBLE_NOTIF_CHANNEL_ID = "BUBBLE_NOTIF_CHANNEL_ID"
    fun createBubble() {
        if (!BuildCompat.isAtLeastQ()) {
            return;
        }
        // Create bubble intent
        val target = Intent(application, BubbleActivity::class.java)
        val bubbleIntent = PendingIntent.getActivity(application, 0, target, 0 /* flags */)

        // Create bubble metadata
        val bubbleData = Notification.BubbleMetadata.Builder()
                .setDesiredHeight(600)
                // Note: although you can set the icon is not displayed in Q Beta 2
                .setIcon(Icon.createWithResource(application, R.drawable.sample1))
                .setIntent(bubbleIntent)
                .build()

        // Create notification
        val chatBot = Person.Builder()
            .setBot(true)
            .setName("BubbleBot")
            .setImportant(true)
            .build()

        val builder = Notification.Builder(application, BUBBLE_NOTIF_CHANNEL_ID)
            .setContentIntent(bubbleIntent)
            .setSmallIcon(Icon.createWithResource(application, R.drawable.sample1))
            .setBubbleMetadata(bubbleData)
            .addPerson(chatBot)
            .build()

        val notificationManager = (application.getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager
        notificationManager.notify(1, builder)
    }
}