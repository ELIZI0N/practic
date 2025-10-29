package com.example.practic

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.practic.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: BottomNavigationView
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navigationView = binding.navViewSide

        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navView.itemTextAppearanceInactive = R.style.BottomNavigationViewInactive
        navView.itemTextAppearanceActive = R.style.BottomNavigationViewActive

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_manga, R.id.navigation_menu
            )
        )
        navView.setupWithNavController(navController)

        navigationView.setNavigationItemSelectedListener(this)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_menu -> {
                    drawerLayout.openDrawer(GravityCompat.END)
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.getStringExtra("SELECT_TAB")) {
            "home" -> {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                navController.navigate(R.id.navigation_home)

                navView.selectedItemId = R.id.navigation_home
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_add -> {
                openAddMangaActivity()
                drawerLayout.closeDrawer(GravityCompat.END)
            }
            R.id.nav_edit -> {
                openEditMangaActivity()
                drawerLayout.closeDrawer(GravityCompat.END)
            }
        }
        return true
    }

    private fun openAddMangaActivity() {
        val intent = Intent(this, AddMangaActivity::class.java)
        startActivity(intent)
    }

    private fun openEditMangaActivity() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.navigation_edit_selection)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }
}