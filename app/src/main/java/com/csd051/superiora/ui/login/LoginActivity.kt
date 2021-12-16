package com.csd051.superiora.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.csd051.superiora.R
import com.csd051.superiora.databinding.ActivityLoginBinding
import com.csd051.superiora.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LoginViewModel::class.java]

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnSignIn.setOnClickListener {
            validator()
        }
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

        viewModel.login(email, password)

        viewModel.isLoadingLogin.observe(this, {
            showLoading(it)
        })

        viewModel.getMessageLogin.observe(this, { message ->
            showMessage(message)
        })

        binding.edtEmailLogin.text.clear()
        binding.edtPassword.text.clear()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}