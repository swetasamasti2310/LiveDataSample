package com.example.swetakumari.livedatasample.Network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.swetakumari.livedatasample.R
import kotlinx.android.synthetic.main.activity_network_operations.*

class NetworkOperationsActivity : AppCompatActivity(), DownloadCallback {

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private var networkFragment: NetworkFragment? = null

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private var downloading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_operations)
        networkFragment = NetworkFragment.getInstance(
            supportFragmentManager,
            "https://www.google.com"
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.network, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            // When the user clicks FETCH, fetch the first 500 characters of
            // raw HTML from www.google.com.
            R.id.fetch_action -> {
                startDownload()
                return true
            }

            // Clear the text and cancel download.
            R.id.clear_action -> {
                finishDownloading()
                dataText?.setText("")
                return true
            }
        }
        return false
    }

    private fun startDownload() {
        if (!downloading) {
            // Execute the async download.
            networkFragment?.startDownload()
            downloading = true
        }
    }

    override fun updateFromDownload(result: String?) {
        result?.let { dataText.setText(result) } ?: dataText.setText(getString(R.string.connection_error))
    }

    override fun getActiveNetworkInfo(): NetworkInfo? {
        // "as" keyword used for typecast
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo
    }

    override fun onProgressUpdate(progressCode: Int, percentComplete: Int) {
        when(progressCode) {
            ERROR -> {
            }
            CONNECT_SUCCESS -> {
            }
            GET_INPUT_STREAM_SUCCESS -> {
            }
            PROCESS_INPUT_STREAM_IN_PROGRESS -> {
                dataText.setText("$percentComplete%")
            }
            PROCESS_INPUT_STREAM_SUCCESS -> {
            }
        }
    }

    override fun finishDownloading() {
        downloading = false;
        networkFragment?.cancelDownload()
    }

}


