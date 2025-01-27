package com.example.practic

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.practic.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        navView.itemTextAppearanceInactive = R.style.BottomNavigationViewInactive
        navView.itemTextAppearanceActive = R.style.BottomNavigationViewActive


        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_manga, R.id.navigation_search, R.id.navigation_accaunt
            )
        )
        navView.setupWithNavController(navController)
    }
}
