package com.csd051.superiora.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel(){
    private lateinit var mAuth: FirebaseAuth

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoading

    private val message = MutableLiveData<String>()
    val getMessageLogin: LiveData<String> = message

    //Todo masukin fun login ke repository
    fun login(email: String, password: String) {
        _isLoading.value = true
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _isLoading.value = false
            message.value = "Berhasil Login"
        }.addOnFailureListener {
            _isLoading.value = false
            message.value = "Email atau Password salah!"
        }
    }
}