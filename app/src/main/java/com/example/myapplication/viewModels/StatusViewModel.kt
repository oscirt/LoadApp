package com.example.myapplication.viewModels

import android.app.NotificationManager
import android.content.Context
import android.util.Log
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
                try {
                    val body = when (chosenSource) {
                        Source.GLIDE -> DownloadProjectsReadme.service.getGlideReadme()
                        Source.UDACITY -> DownloadProjectsReadme.service.getUdacityReadme()
                        Source.RETROFIT -> DownloadProjectsReadme.service.getRetrofitReadme()
                        else -> throw java.lang.IllegalStateException()
                    }
                    _status.postValue(1f)
                    code = body.code()
                    notificationManager.createNotification(
                        "File ${chosenSource.toString()} downloaded",
                        context,
                        chosenSource!!.ordinal,
                        code
                    )
                } catch (e: Exception) {
                    notificationManager.createNotification(
                        "File ${chosenSource.toString()} is not downloaded",
                        context,
                        chosenSource!!.ordinal,
                        404
                    )
                    Log.w(TAG, "download: ${e.javaClass}: ${e.message}")
                    _status.postValue(0f)
                }
            }
        }
    }
}