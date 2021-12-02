package com.csd051.superiora.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.csd051.superiora.R
import com.csd051.superiora.data.entity.User
import com.csd051.superiora.databinding.ActivityRegisterBinding
import com.csd051.superiora.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
//            RegisterViewModel::class.java
//        )

        mAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            validator()
        }
    }

    private fun validator() {
        when {
            binding.edtName.text.toString().isEmpty() -> {
                binding.edtName.error = getString(R.string.tv_name_null)
                binding.edtName.requestFocus()
                return
            }
            binding.edtEmail.text.toString().isValidEmail() -> {
                binding.edtEmail.error = getString(R.string.tv_email_null)
                binding.edtEmail.requestFocus()
                return
            }
            binding.edtPasswordRegister.text.toString().isEmpty() -> {
                binding.edtPasswordRegister.error = getString(R.string.tv_pass_null)
                binding.edtPasswordRegister.requestFocus()
                return
            }
            binding.edtRepeatPassRegister.text.toString().isEmpty() -> {
                binding.edtRepeatPassRegister.error = getString(R.string.tv_repeatpass_null)
                binding.edtRepeatPassRegister.requestFocus()
                return
            }
            !isValidPassword(binding.edtPasswordRegister.text.toString()) -> {
                binding.edtPasswordRegister.error = getString(R.string.passvalidate)
                binding.edtPasswordRegister.requestFocus()
                return
            }
            binding.edtPasswordRegister.text.toString() != binding.edtRepeatPassRegister.text.toString() -> {
                binding.edtPasswordRegister.error = getString(R.string.pass_notmatch)
                binding.edtPasswordRegister.requestFocus()
                binding.edtRepeatPassRegister.error = getString(R.string.pass_notmatch)
                binding.edtRepeatPassRegister.requestFocus()
                return
            }
            else -> {
                register()
            }
        }
    }

    private fun String.isValidEmail() =
        isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(this).matches()


    fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.currentUser
    }

    private fun register() {
        binding.progressBar.visibility = View.VISIBLE
        val nama = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPasswordRegister.text.toString()

        val user = User(nama, email, password, "")

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(this, "sukses", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }.addOnFailureListener {
            Toast.makeText(this, "gagal", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        }
    }
}