package com.csd051.superiora.ui.user

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.User
import com.csd051.superiora.databinding.ActivityEditUserBinding
import com.csd051.superiora.ui.home.home.HomeActivity
import com.csd051.superiora.viewmodel.ViewModelFactory
import java.io.IOException

class EditProfileUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding
    private lateinit var viewModelProfile: EditProfileUserViewModel
    private val imageDummy =
        "https://firebasestorage.googleapis.com/v0/b/superiora-30875.appspot.com/o/icon_user%2Ficon.png?alt=media&token=e732dded-f66b-4683-954f-70f6a588f67d"
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.edit_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModelProfile = ViewModelProvider(this, factory)[EditProfileUserViewModel::class.java]

        viewModelProfile.getUser().observe(this, { data ->
            if (data != null) {
                Glide.with(this)
                    .load(
                        if (data.urlPhoto == "") {
                            imageDummy
                        } else {
                            data.urlPhoto
                        }
                    )
                    .into(
                        binding.userImgPhoto
                    )
                binding.userEdName.setText(data.nama)
                binding.userEdEmail.text = data.email
            }
        })

        binding.userBtnChangeProfile.setOnClickListener {
           getPhoto()
        }

        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
        }

        binding.logout.setOnClickListener {
            showDialog()
        }
    }

    private fun getPhoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    private fun updateProfile() {
        binding.progressBar.visibility = View.VISIBLE
        viewModelProfile.getUser().observe(this, { data ->
            val user = User(
                data.id,
                data.id_firebase,
                binding.userEdName.text.toString(),
                data.email,
                data.password,
                data.urlPhoto
            )
            if (filePath != null) {
                filePath?.let { it1 ->
                    viewModelProfile.updateUser(this, user, it1)
                }
            } else {
                viewModelProfile.updateUserWithNoImage(this, user)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            // Get the Uri of data
            filePath = data.data!!
            try {
                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filePath
                    )
                binding.userImgPhoto.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
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

    private fun showDialog() {
        val builder = AlertDialog.Builder(this@EditProfileUserActivity)
        builder.setMessage(getString(R.string.logout_validation))
        builder.setCancelable(true)
        builder.setPositiveButton(
            R.string.yes
        ) { _, _ ->
            viewModelProfile.logout(binding.userEdEmail.text.toString())
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            Toast.makeText(this, getString(R.string.success_logout), Toast.LENGTH_SHORT).show()
            finish()
        }
        builder.setNegativeButton(
            R.string.no
        ) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 22
    }
}