package com.example.myapplication

import android.os.Bundle
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.viewModels.StatusViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val viewModel: StatusViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHost.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.baseFragment, R.id.downloadStatusFragment))
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() or super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        if (viewModel.chosenSource != null) {
            findViewById<RadioGroup>(R.id.radio_group_download_source)
                .check(viewModel.chosenSource!!.ordinal)
        }
    }
}