package com.example.swetakumari.livedatasample

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class NameViewModel : ViewModel() {

    // Create a LiveData with a String
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val liveData1 = MutableLiveData<String>()
    val liveData2 = MutableLiveData<String>()

    val liveDataMerger = MediatorLiveData<String>()
    // Rest of the ViewModel...
}