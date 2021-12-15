package com.csd051.superiora.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.csd051.superiora.R
import com.csd051.superiora.databinding.ActivityHomeBinding
import com.csd051.superiora.ui.login.LoginActivity
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {
    //Test
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_today, R.id.nav_task, R.id.nav_settings, R.id.nav_android, R.id.nav_angular, R.id.nav_backend,
                R.id.nav_dba, R.id.nav_devops, R.id.nav_frontend, R.id.nav_go, R.id.nav_java, R.id.nav_python,
                R.id.nav_react
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun actionHeaderClick(view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_today_schedule, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}