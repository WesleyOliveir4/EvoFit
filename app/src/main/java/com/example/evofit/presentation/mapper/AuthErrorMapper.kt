package com.example.evofit.presentation.mapper

import android.content.Context
import com.example.evofit.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class AuthErrorMapper(private val context: Context) {
    fun map(throwable: Throwable): String {
        return when (throwable) {
            is FirebaseAuthWeakPasswordException -> context.getString(R.string.error_auth_weak_password)
            is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.error_auth_invalid_credentials)
            is FirebaseAuthInvalidUserException -> context.getString(R.string.error_auth_invalid_credentials)
            is FirebaseAuthUserCollisionException -> context.getString(R.string.error_auth_registration_failed)
            is FirebaseAuthException -> {
                when (throwable.errorCode) {
                    "ERROR_INVALID_EMAIL",
                    "ERROR_WRONG_PASSWORD",
                    "ERROR_USER_NOT_FOUND" -> context.getString(R.string.error_auth_invalid_credentials)
                    "ERROR_EMAIL_ALREADY_IN_USE" -> context.getString(R.string.error_auth_registration_failed)
                    "ERROR_USER_DISABLED" -> context.getString(R.string.error_auth_user_disabled)
                    "ERROR_TOO_MANY_REQUESTS" -> context.getString(R.string.error_auth_too_many_requests)
                    "ERROR_WEAK_PASSWORD" -> context.getString(R.string.error_auth_weak_password)
                    else -> context.getString(R.string.error_auth_generic)
                }
            }
            is FirebaseNetworkException -> context.getString(R.string.error_auth_network_error)
            else -> context.getString(R.string.error_auth_generic)
        }
    }
}
