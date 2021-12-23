package com.csd051.superiora.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository

class LoginViewModel(private val repository: SuperioraRepository) : ViewModel(){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoadingLogin: LiveData<Boolean> = _isLoading

    fun login(context: Context, email: String, password: String) {
        _isLoading.value = true
        repository.login(context, email, password)
    }

    fun forgotPassword(context: Context, email: String) = repository.forgotPassword(context, email)
}