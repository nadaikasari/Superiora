package com.csd051.superiora.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.csd051.superiora.R
import com.csd051.superiora.databinding.ActivityEditUserBinding
import com.csd051.superiora.viewmodel.ViewModelFactory

class EditUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding
    private lateinit var viewModel: EditUserViewModel
    private val imageDummy =
        "https://firebasestorage.googleapis.com/v0/b/superiora-30875.appspot.com/o/icon_user%2Ficon.png?alt=media&token=e732dded-f66b-4683-954f-70f6a588f67d"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.edit_task)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[EditUserViewModel::class.java]

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
                        binding.userImgPhoto
                    )
                binding.userEdName.setText(data.nama)
                binding.userEdEmail.setText(data.email)
            }
        })

        binding.userBtnChangeProfile.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            //todo Deprecated
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Select Image from here..."
                ),
                PICK_IMAGE_REQUEST
            )
        }

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

    companion object {
        private val PICK_IMAGE_REQUEST = 22

    }
}