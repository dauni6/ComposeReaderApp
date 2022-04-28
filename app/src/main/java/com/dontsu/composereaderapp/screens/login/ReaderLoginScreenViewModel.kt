package com.dontsu.composereaderapp.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dontsu.composereaderapp.data.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
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
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }.addOnFailureListener { exception ->
                    if (exception is FirebaseAuthInvalidCredentialsException || exception is FirebaseAuthInvalidUserException) {
                        onResult(false)
                    }
                }.addOnCanceledListener {

                }
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(false)
        }

    }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        try {
            if (loading.value == false) {
                _loading.value = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // ex) me@gmail.com
                            val displayName = task.result.user?.email?.substringBefore("@")
                            createUser(displayName)
                            onResult(true)
                        } else {
                            Log.d("TEST", "계정 생성 실패1")
                            onResult(false)
                        }
                        _loading.value = false
                    }.addOnFailureListener {
                        Log.d("TEST", "계정 생성 실패2")
                        onResult(false)
                    }.addOnCanceledListener {
                        Log.d("TEST", "계정 생성 실패3")
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(false)
        }
    }

    private fun createUser(displayName: String?) {
        if (displayName.isNullOrEmpty()) return

        val userId = auth.currentUser?.uid

        if (userId.isNullOrEmpty()) return // 단지 return만 할게아니라 안 된다고 Toast를 띄우던 뭐든 해야겠넹

        val user = MUser(
            id = null,
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is great.",
            profession = "Android Developer"
        ).toMap()

        FirebaseFirestore.getInstance().collection(FS_COLLECTION_USERS)
            .add(user)

    }

    companion object {
        const val FS_COLLECTION_USERS = "users"
    }


}
