package com.csd051.superiora.data

import com.csd051.superiora.data.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class SuperioraRepository {

    private lateinit var firebase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    fun register(users: User) {
        mAuth = FirebaseAuth.getInstance()
        firebase = FirebaseFirestore.getInstance()

        val user = mAuth.currentUser
        val df: DocumentReference = firebase.collection("users").document(user!!.uid)
        val userInfo: MutableMap<String, Any> = HashMap()
        userInfo["name"] = users.nama
        userInfo["photo"] = ""
        df.set(userInfo)
    }


}