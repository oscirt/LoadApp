package com.example.myapplication.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentBaseBinding
import com.example.myapplication.viewModels.Source
import com.example.myapplication.viewModels.StatusViewModel

class BaseFragment : Fragment() {

    private lateinit var binding: FragmentBaseBinding
    private val viewModel: StatusViewModel by viewModels()
    private lateinit var notificationManager: NotificationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().intent.extras?.let {
            findNavController().navigate(
                BaseFragmentDirections.actionBaseFragmentToDownloadStatusFragment(
                    it.getInt("source"),
                    it.getInt("code")
                )
            )
            return null
        } ?: run {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_base,
                container,
                false
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
                requireContext(), NotificationManager::class.java) as NotificationManager

            createNotificationChannel(
                getString(R.string.download_notification_channel_id),
                getString(R.string.download_notification_channel_name)
            )

            binding.downloadButton.setOnClickListener {
                if (binding.radioGroupDownloadSource.checkedRadioButtonId != -1) {
                    viewModel.download(
                        requireContext(),
                        notificationManager
                    )
                } else {
                    Toast(requireContext()).apply {
                        duration = Toast.LENGTH_SHORT
                        setText("Please select the file to download")
                        show()
                    }
                }
            }
            return binding.root
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