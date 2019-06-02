package com.example.swetakumari.livedatasample.Network

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment

import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

private const val TAG = "NetworkFragment"
private const val URL_KEY = "UrlKey"

class NetworkFragment : Fragment() {

    private var mCallback: DownloadCallback? = null
    private var downloadTask: DownloadTask? = null
    private var urlString: String? = null

    companion object {
        /**
         * Static initializer for NetworkFragment that sets the URL of the host it will be
         * downloading from.
         */

        fun getInstance(fragmentManager: android.support.v4.app.FragmentManager, url: String): NetworkFragment {
            val networkFragment = NetworkFragment()
            val args  = Bundle()
            args.putString(URL_KEY, url)
            networkFragment.arguments = args
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit()
            return networkFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        urlString = arguments?.getString(URL_KEY)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // Host Activity will handle callbacks from task.
        mCallback = context as? DownloadCallback
    }

    override fun onDetach() {
        super.onDetach()
        // Clear reference to host Activity to avoid memory leak.
        mCallback = null
    }

    override fun onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelDownload()
        super.onDestroy()
    }

    /**
     * Start non-blocking execution of DownloadTask.
     */
    fun startDownload() {
        cancelDownload()
        mCallback?.also { callback ->
            downloadTask = DownloadTask(callback).apply {
                execute(urlString)
            }
        }
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    fun cancelDownload() {
        downloadTask?.cancel(true)
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class DownloadTask(callback: DownloadCallback): AsyncTask<String, Int, DownloadTask.Result>()
    {

        private var mCallback: DownloadCallback? = null

        init {
            setCallback(callback)
        }

        /**
         * Wrapper class that serves as a union of a result value and an exception. When the download
         * task has completed, either the result value or exception can be a non-null value.
         * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
         */
        internal fun setCallback(callback: DownloadCallback) {
            mCallback = callback
        }

        /**
         * Cancel background network operation if we do not have network connectivity.
         */
        override fun onPreExecute() {
            if (mCallback != null) {
                val networkInfo = mCallback?.getActiveNetworkInfo()
                if (networkInfo?.isConnected == false
                    || networkInfo?.type != ConnectivityManager.TYPE_WIFI
                    && networkInfo?.type != ConnectivityManager.TYPE_MOBILE) {
                    // If no connectivity, cancel task and update Callback with null data.
                    mCallback?.updateFromDownload(null)
                    cancel(true)
                }
            }
        }

        /**
         * Defines work to perform on the background thread.
         */
        override fun doInBackground(vararg urls: String?): Result? {
            var result: Result? = null
            if (!isCancelled && urls.isNotEmpty()) {
                val urlString = urls[0]
                result = try {
                    val url = URL(urlString)
                    val resultString = downloadUrl(url)
                    if (resultString != null) {
                        Result(resultString)
                    } else {
                        throw IOException("No Response Received")
                    }
                } catch (e: Exception) {
                    Result(e)
                }
            }

            return result
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */
        private fun downloadUrl(url: URL): String? {
            var connection: HttpURLConnection? = null
            return try {
                connection = url.openConnection() as? HttpURLConnection
                connection?.run {
                    // Timeout for reading InputStream arbitrarily set to 3000ms.
                    readTimeout = 3000
                    // Timeout for connection.connect() arbitrarily set to 3000ms.
                    connectTimeout = 3000
                    // For this use case, set HTTP method to GET.
                    requestMethod = "GET"
                    // Already true by default but setting just in case; needs to be true since this request
                    // is carrying an input (response) body.
                    doInput = true
                    // Open communications link (network traffic occurs here).
                    connect()
                    publishProgress(CONNECT_SUCCESS)
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw IOException("HTTP error code: $responseCode")
                    }
                    // Retrieve the response body as an InputStream.
                    publishProgress(GET_INPUT_STREAM_SUCCESS, 0)

                    inputStream?.let { stream ->
                        // Converts Stream to String with max length of 500.
                        readStream(stream, 500) }
                }
            }
            finally {
                // Close Stream and disconnect HTTPS connection.
                connection?.inputStream?.close()
                connection?.disconnect()
            }
        }

        /**
         * Converts the contents of an InputStream to a String.
         */
        @Throws(IOException::class, UnsupportedEncodingException::class)
        private fun readStream(stream: InputStream, maxReadSize: Int): String? {
            val reader: Reader? = InputStreamReader(stream, "UTF-8")
            val rawBuffer = CharArray(maxReadSize)
            val buffer = StringBuffer()
            var readSize = reader?.read(rawBuffer) ?: -1
            var maxReadBytes = maxReadSize
            while (readSize != -1 && maxReadBytes > 0) {
                if (readSize > maxReadBytes) {
                    readSize = maxReadBytes
                }
                buffer.append(rawBuffer, 0, readSize)
                maxReadBytes -= readSize
                readSize = reader?.read(rawBuffer) ?: -1
            }
            return buffer.toString()
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        override fun onPostExecute(result: Result?) {
            mCallback?.apply {
                result?.mException?.also { exception ->
                    updateFromDownload(exception.message)
                    return
                }
                result?.mResultValue?.also { resultValue ->
                    updateFromDownload(resultValue)
                    return
                }
                finishDownloading()
            }
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        override fun onCancelled() {

        }

        internal class Result
        {
            var mResultValue: String? = null
            var mException: Exception? = null

            constructor(resultValue: String) {
                mResultValue = resultValue
            }

            constructor(exception: Exception) {
                mException = exception
            }

        }
    }
}
