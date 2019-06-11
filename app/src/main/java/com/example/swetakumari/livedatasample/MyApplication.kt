package com.example.swetakumari.livedatasample

import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate

class MyApplication: Application() {
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate();
        context = applicationContext
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

}