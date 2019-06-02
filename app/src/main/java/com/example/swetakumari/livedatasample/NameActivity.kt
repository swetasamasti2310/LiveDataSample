package com.example.swetakumari.livedatasample

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_name.*

class NameActivity : AppCompatActivity() {

    private lateinit var mModel: NameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)

        // Other code to setup the activity...

        // Get the ViewModel.

        mModel = ViewModelProviders.of(this).get(NameViewModel::class.java)

        // Create the observer which updates the UI.
        val nameObserver = Observer<String> { newName ->
            mNameTextView.text = newName
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        mModel.currentName.observe(this, nameObserver)

        mButton.setOnClickListener {
            val anotherName = "John Doe"
            mModel.currentName.setValue(anotherName)
        }

        val mediatorObserver = Observer<String> { value -> mModel.liveDataMerger.setValue(value)}

        mModel.liveDataMerger.addSource(mModel.liveData1, mediatorObserver)

        mModel.liveDataMerger.addSource(mModel.liveData2, mediatorObserver)

        mButton1.setOnClickListener {
            val anotherName = "Test name 1"
            mModel.liveData1.setValue(anotherName)
        }

        mButton2.setOnClickListener {
            val anotherName = "Live data 2"
            mModel.liveData2.setValue(anotherName)
        }

        mModel.liveDataMerger.observe(this, nameObserver)
    }
}