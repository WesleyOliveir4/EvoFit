package com.guaracode.evofit.presentation.ui.feature.authentication.google

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "EvoFit_Google"

actual class GoogleSignInHandler(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    actual suspend fun signIn(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
            val serverClientId = if (resId != 0) context.getString(resId) else "default_web_client_id"
            Log.d(TAG, "Using serverClientId: $serverClientId")

            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            handleSignIn(result)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "GetCredentialException during signIn: type=${e.type}, message=${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Exception during signIn: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun handleSignIn(result: GetCredentialResponse): Result<String> {
        val credential = result.credential
        Log.d(TAG, "handleSignIn credential type: ${credential.type}")

        return when {
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    Log.d(TAG, "GoogleIdTokenCredential created successfully from data")
                    Result.success(googleIdTokenCredential.idToken)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to parse GoogleIdTokenCredential", e)
                    Result.failure(e)
                }
            }
            else -> {
                Result.failure(Exception("Tipo de credencial inválido: ${credential.type}"))
            }
        }
    }

    actual suspend fun signOut() {
        try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        } catch (_: Exception) {}
    }
}
