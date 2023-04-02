package com.example.myapplication.activities

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDownloadStatusBinding

class DownloadStatusActivity : AppCompatActivity() {

    private companion object {
        private const val TAG = "DownloadStatusActivity"
    }

    private lateinit var binding: ActivityDownloadStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_download_status)

        val notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java) as NotificationManager
        notificationManager.cancelAll()

        val bundle = intent.extras!!.getBundle("DOWNLOAD_STATUS")!!

        binding.fileName.text = when (bundle.getInt("source")) {
            0 -> getString(R.string.glide)
            1 -> getString(R.string.loadApp)
            2 -> getString(R.string.retrofit)
            else -> throw java.lang.IllegalStateException()
        }
        binding.status.text = if (bundle.getInt("code") == 200) "Success" else "Fail"
        
        binding.buttonReturn.setOnClickListener {
            finish()
        }
    }
}