package com.example.swetakumari.livedatasample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.support.v7.app.AppCompatDelegate.MODE_NIGHT_NO
import android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.swetakumari.livedatasample.Bubble.BubbleActivity
import com.example.swetakumari.livedatasample.Bubble.BubbleHelper
import com.example.swetakumari.livedatasample.Network.NetworkOperationsActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel();
        btn1.setOnClickListener {
            toggleNightMode();
        }

        btn2.setOnClickListener {
            val intent = Intent(this, NameActivity::class.java)
            startActivity(intent)
        }

        btn3.setOnClickListener {
            val intent = Intent(this, SwitchMapActivity::class.java)
            startActivity(intent)
        }

        btn4.setOnClickListener {
            val intent = Intent(this, NetworkOperationsActivity::class.java)
            startActivity(intent)
        }

        btn5.setOnClickListener {
            BubbleHelper.createBubble(this.applicationContext);
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.bubble_channel)
            val descriptionText = getString(R.string.bubble_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(BubbleHelper.BUBBLE_NOTIF_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun toggleNightMode() {
        if (MODE_NIGHT_YES.equals(AppCompatDelegate.getDefaultNightMode())) {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        }
    }

}
