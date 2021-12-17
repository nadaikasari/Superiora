package com.csd051.superiora.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.User

class EditUserViewModel (private val repository: SuperioraRepository) : ViewModel() {

    fun getUser(): LiveData<User> = repository.getUser()

}