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
import com.example.swetakumari.livedatasample.SwitchMapActivity

object BubbleHelper {
    val BUBBLE_NOTIF_CHANNEL_ID = "BUBBLE_NOTIF_CHANNEL_ID"
    fun createBubble(application: Context) {
        if (!BuildCompat.isAtLeastQ()) {
            return;
        }
        // Create bubble intent
        val target = Intent(application, BubbleActivity::class.java)
        val bubbleIntent = PendingIntent.getActivity(application, 0, target, 0 /* flags */)

        // Create bubble metadata
        val bubbleData = Notification.BubbleMetadata.Builder()
                // this sets the height of layout on click of bubble
                .setDesiredHeight(100)
                // Note: although you can set the icon is not displayed in Q Beta 2
                .setIcon(Icon.createWithResource(application, R.drawable.sample1))
                // this sets the intent for the expanded view of the bubble
                .setIntent(bubbleIntent)
                // Auto expandable bubble opens with its expanded view else appears only bubble on click of which it will expand
                .setAutoExpandBubble(false)
                .setSuppressInitialNotification(true)
                .build()

        // Create notification
        val chatBot = Person.Builder()
            .setBot(true)
            .setName("BubbleBot")
            .setImportant(true)
            .build()

        val targetContentIntent = Intent(application, SwitchMapActivity::class.java)
        val contentIntent = PendingIntent.getActivity(application, 0, targetContentIntent, 0 /* flags */)
        val builder = Notification.Builder(application, BUBBLE_NOTIF_CHANNEL_ID)
            .setContentIntent(contentIntent)
            .setSmallIcon(Icon.createWithResource(application, R.drawable.sample1))
            .setBubbleMetadata(bubbleData)
            .addPerson(chatBot)
            .build()

        val notificationManager = (application.getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager
        notificationManager.notify(101, builder)
    }
}