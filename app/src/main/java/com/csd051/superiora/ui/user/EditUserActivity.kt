package com.csd051.superiora.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.csd051.superiora.R
import com.csd051.superiora.databinding.ActivityEditUserBinding
import com.csd051.superiora.viewmodel.ViewModelFactory

class EditUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding
    private lateinit var viewModel: EditUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.edit_task)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[EditUserViewModel::class.java]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}