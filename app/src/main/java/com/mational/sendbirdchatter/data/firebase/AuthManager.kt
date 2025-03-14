package com.mational.sendbirdchatter.data.firebase

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class AuthManager @Inject constructor() {
    private val firebaseAuthInstance = FirebaseAuth.getInstance()

    init {
        firebaseAuthInstance.currentUser?.reload()
    }

    suspend fun createAccount(email: String, password: String): Result<String> =
        suspendCoroutine { continuation ->
            firebaseAuthInstance.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(task.result.user!!.uid))
                    } else {
                        continuation.resume(
                            Result.failure(task.exception ?: Exception("Unknown error"))
                        )
                    }
                }
        }


    suspend fun signIn(email: String, password: String): Result<String> =
        suspendCoroutine { continuation ->
            firebaseAuthInstance.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(task.result.user!!.uid))
                    } else {
                        continuation.resume(
                            Result.failure(task.exception ?: Exception("Unknown error"))
                        )
                    }
                }
        }

    fun autoLogin(): Result<String> {
        val currentUser = firebaseAuthInstance.currentUser
        if (currentUser != null) {
            currentUser.reload()
            return Result.success(currentUser.uid)
        } else {
            return Result.failure(Exception("No user found"))
        }
    }
}