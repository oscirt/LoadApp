package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.fragments.BaseFragmentDirections

class MainActivity : AppCompatActivity() {

    private companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHost.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.baseFragment, R.id.downloadStatusFragment))
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        val type = intent.getStringExtra("TYPE")
        if (type != null && type == "download_notification") {
            val bundle = intent.getBundleExtra("DOWNLOAD_STATUS")!!
            navController.navigate(
                BaseFragmentDirections.actionBaseFragmentToDownloadStatusFragment(
                    bundle.getInt("source"),
                    bundle.getInt("code")
                )
            )
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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() or super.onSupportNavigateUp()
    }
}