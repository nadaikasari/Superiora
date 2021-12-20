package com.csd051.superiora.ui.home.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.csd051.superiora.R
import com.csd051.superiora.databinding.ActivityHomeBinding
import com.csd051.superiora.ui.login.LoginActivity
import com.csd051.superiora.ui.user.EditProfileUserActivity
import com.csd051.superiora.viewmodel.ViewModelFactory
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var isLoginUser: Boolean = false
    private val imageDummy =
        "https://firebasestorage.googleapis.com/v0/b/superiora-30875.appspot.com/o/icon_user%2Ficon.png?alt=media&token=e732dded-f66b-4683-954f-70f6a588f67d"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        setHeaderView()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_today,
                R.id.nav_task,
                R.id.nav_settings,
                R.id.nav_android,
                R.id.nav_angular,
                R.id.nav_backend,
                R.id.nav_dba,
                R.id.nav_devops,
                R.id.nav_frontend,
                R.id.nav_go,
                R.id.nav_java,
                R.id.nav_python,
                R.id.nav_react
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setHeaderView() {
        viewModel.getUser().observe(this, { data ->
            if (data != null) {
                Glide.with(this)
                    .load(
                        if(data.urlPhoto == "") {
                            imageDummy
                        } else {
                            data.urlPhoto
                        }
                    )
                    .into(
                        binding.navView.getHeaderView(0)
                            .findViewById<CircleImageView>(R.id.image_user)
                    )
                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.name_user).text =
                    data.nama
                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.email_user).text =
                    data.email
                isLoginUser = true
            } else {
                Glide.with(this)
                    .load(imageDummy)
                    .into(
                        binding.navView.getHeaderView(0)
                            .findViewById<CircleImageView>(R.id.image_user)
                    )
            }
        })
    }

    fun actionHeaderClick(view: View) {
        if (!isLoginUser) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            startActivity(Intent(this, EditProfileUserActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}