package com.dontsu.composereaderapp.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ReaderLoginScreenViewModel: ViewModel() {
//    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TEST", "success : ${task.result}")
                        // todo : home으로 보내기
                        onResult(true)
                    } else {
                        Log.d("TEST", "failed : ${task.result}")
                        onResult(false)
                    }
                }.addOnFailureListener { exception ->
                    if (exception is FirebaseAuthInvalidCredentialsException || exception is FirebaseAuthInvalidUserException) {
                        onResult(false)
                    }
                }
        } catch (e: Exception) {
            onResult(false)
        }

    }

    fun createUserWithEmailAndPassword() {

    }


}
