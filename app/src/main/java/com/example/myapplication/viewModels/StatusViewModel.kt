package com.example.myapplication.viewModels

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.retrofit.DownloadProjectsReadme
import com.example.myapplication.utils.createNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "STATUS_VIEW_MODEL"

enum class Source {
    GLIDE,
    UDACITY,
    RETROFIT,
}

class StatusViewModel : ViewModel() {
    private var _status = MutableLiveData<Float>()
    val status: LiveData<Float> get() = _status

    private var code = -1
    var chosenSource: Source? = null

    fun download(context: Context, notificationManager: NotificationManager) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _status.postValue(0.6f)
                val bundle = Bundle()
                try {
                    val body = when (chosenSource) {
                        Source.GLIDE -> DownloadProjectsReadme.service.getGlideReadme()
                        Source.UDACITY -> DownloadProjectsReadme.service.getUdacityReadme()
                        Source.RETROFIT -> DownloadProjectsReadme.service.getRetrofitReadme()
                        else -> throw java.lang.IllegalStateException()
                    }
                    _status.postValue(1f)
                    code = body.code()
                    bundle.putBundle("DOWNLOAD_STATUS", bundleOf(
                        Pair("source", chosenSource!!.ordinal),
                        Pair("code", code)
                    ))
                    notificationManager.createNotification(
                        "File ${chosenSource.toString()} downloaded",
                        context,
                        bundle
                    )
                } catch (e: Exception) {
                    bundle.putBundle("DOWNLOAD_STATUS", bundleOf(
                        Pair("source", chosenSource!!.ordinal),
                        Pair("code", 404)
                    ))
                    notificationManager.createNotification(
                        "File ${chosenSource.toString()} is not downloaded",
                        context,
                        bundle
                    )
                    Log.w(TAG, "download: ${e.javaClass}: ${e.message}")
                    _status.postValue(0f)
                }
            }
        }
    }
}