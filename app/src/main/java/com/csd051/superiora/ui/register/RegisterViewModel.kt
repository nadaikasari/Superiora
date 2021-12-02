package com.csd051.superiora.ui.register

import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.User

class RegisterViewModel(private val repository: SuperioraRepository) : ViewModel() {

    fun register(user : User) {
        repository.register(user)
    }
}