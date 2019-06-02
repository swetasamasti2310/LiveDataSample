package com.example.swetakumari.livedatasample

import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_switch_map.*
import android.arch.lifecycle.MutableLiveData



class SwitchMapActivity : AppCompatActivity() {

    private lateinit var mModel: NameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_map)

        mModel = ViewModelProviders.of(this).get(NameViewModel::class.java)

        val nameMapObserver = Observer<String> { mappedName ->
            mapResult.text = mappedName
        }

        val nameSwitchMapObserver = Observer<String> { mappedName ->
            switchMapResult.text = mappedName
        }

        inputBtn.setOnClickListener {
            mModel.currentName.setValue(inputName.text.toString())
        }

        Transformations.map(mModel.currentName, String::toUpperCase).observe(this, nameMapObserver)

        Transformations.switchMap(mModel.currentName, this::getUpperCaseAppended).observe(this, nameSwitchMapObserver)

    }

    fun getUpperCaseAppended(str : String) : MutableLiveData<String> {
        val liveData = MutableLiveData<String>()
        liveData.setValue(str + " " + str.toUpperCase())
        return liveData
    }
}
