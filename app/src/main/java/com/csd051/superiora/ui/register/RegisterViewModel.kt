package com.csd051.superiora.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RegisterViewModel : ViewModel() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var firebase: FirebaseFirestore

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val message = MutableLiveData<String>()
    val getMessage: LiveData<String> = message

    fun register(user: User) {
        _isLoading.value = true
        mAuth = FirebaseAuth.getInstance()
        firebase = FirebaseFirestore.getInstance()
        mAuth.createUserWithEmailAndPassword(user.email, user.password).addOnSuccessListener {
            _isLoading.value = false
            val users = mAuth.currentUser
            val df: DocumentReference = firebase.collection("users").document(users!!.uid)
            val userInfo: MutableMap<String, Any> = HashMap()
            userInfo["name"] = user.nama
            userInfo["photo"] = ""
            df.set(userInfo)
            message.value = "Register sukses, silahkan login"
        }.addOnFailureListener {
            _isLoading.value = false
            message.value = "gagal mendaftarkan akun, Silahkan coba lagi"
        }
    }
}