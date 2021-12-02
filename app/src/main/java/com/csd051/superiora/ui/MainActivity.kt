package com.csd051.superiora.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.csd051.superiora.R
import com.csd051.superiora.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    //Tambahan Dari Iqbal
    // Tambahan Line 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, LoginActivity::class.java))
        //update master
    }
}