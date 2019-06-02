package com.example.swetakumari.livedatasample

import android.content.Intent
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
            BubbleHelper.createBubble();
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
