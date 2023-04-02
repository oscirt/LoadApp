package com.example.myapplication.activities

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityBaseBinding
import com.example.myapplication.viewModels.Source
import com.example.myapplication.viewModels.StatusViewModel

class MainActivity : AppCompatActivity() {

    private companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var notificationManager: NotificationManager
    private lateinit var binding: ActivityBaseBinding

    private val viewModel: StatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_base
        )

        binding.viewModel = viewModel

        binding.radioGroupDownloadSource.setOnCheckedChangeListener { _, checkedId ->
            viewModel.chosenSource = when (checkedId) {
                R.id.radio_button_glide_source -> Source.GLIDE
                R.id.radio_button_loadApp_source -> Source.UDACITY
                R.id.radio_button_retrofit_source -> Source.RETROFIT
                else -> null
            }
        }

        notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java) as NotificationManager

        createNotificationChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name)
        )

        binding.downloadButton.setOnClickListener {
            if (binding.radioGroupDownloadSource.checkedRadioButtonId != -1) {
                viewModel.download(
                    this,
                    notificationManager
                )
            } else {
                Toast(this).apply {
                    duration = Toast.LENGTH_SHORT
                    setText("Please select the file to download")
                    show()
                }
            }
        }

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted) {
                    Toast.makeText(
                        this,
                        "Download notifications will not come",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        if (Build.VERSION.SDK_INT > 32) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onCreate: permission already was granted")
                }
            else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun createNotificationChannel(
        channelId: String,
        channelName: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(false)
                description = "Download files notifications"
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}