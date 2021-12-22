package com.csd051.superiora.ui.user

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.csd051.superiora.data.SuperioraRepository
import com.csd051.superiora.data.entity.User

class EditProfileUserViewModel(private val repository: SuperioraRepository) : ViewModel() {

    fun getUser(): LiveData<User> = repository.getUser()

    fun updateUser(context: Context, user: User, filepath: Uri) =
        repository.updateUser(context, user, filepath)

    fun updateUserWithNoImage(context: Context, user: User) =
        repository.updateDataUserWithNoUpdateImage(context, user)

    fun logout(email: String) = repository.deleteUser(email)

}