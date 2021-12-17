package com.csd051.superiora.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.csd051.superiora.R
import com.csd051.superiora.databinding.ActivityLoginBinding
import com.csd051.superiora.ui.register.RegisterActivity
import com.csd051.superiora.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnSignIn.setOnClickListener {
            validator()
        }

        viewModel.getUser().observe(this, { data->
            if (data != null) {
                viewModel.logout(data.email)
            }
        })

    }

    private fun validator() {
        when {
            binding.edtEmailLogin.text.toString().isEmpty() -> {
                binding.edtEmailLogin.error = getString(R.string.tv_email_null)
                binding.edtEmailLogin.requestFocus()
                return
            }
            binding.edtPassword.text.toString().isEmpty() -> {
                binding.edtPassword.error = getString(R.string.tv_pass_null)
                binding.edtPassword.requestFocus()
                return
            }
            else -> {
                login()
            }
        }
    }

    private fun login() {
        val email = binding.edtEmailLogin.text.toString()
        val password = binding.edtPassword.text.toString()

        viewModel.login(this, email, password)

        viewModel.isLoadingLogin.observe(this, {
            showLoading(it)
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}