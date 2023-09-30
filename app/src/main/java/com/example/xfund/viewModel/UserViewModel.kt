package com.example.xfund.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserViewModel : ViewModel() {
    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> get() = _currentUser

    init {
        // Initialize the _currentUser LiveData with the current user
        _currentUser.value = FirebaseAuth.getInstance().currentUser
    }

    fun setUser(user: FirebaseUser?) {
        _currentUser.value = user
    }


}