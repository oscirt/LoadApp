package com.example.myapplication.fragments

import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDownloadStatusBinding

class DownloadStatusFragment : Fragment() {

    private lateinit var binding: FragmentDownloadStatusBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_download_status,
            container,
            false
        )

        val notificationManager = ContextCompat.getSystemService(
            requireContext(), NotificationManager::class.java) as NotificationManager
        notificationManager.cancelAll()

        val bundle = requireArguments().getBundle("DOWNLOAD_STATUS")

        binding.fileName.text = when (bundle?.getInt("source")) {
            0 -> getString(R.string.glide)
            1 -> getString(R.string.loadApp)
            2 -> getString(R.string.retrofit)
            else -> throw java.lang.IllegalStateException()
        }
        binding.status.text = if (bundle.getInt("code") == 200) "Success" else "Fail"

        binding.buttonReturn.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}