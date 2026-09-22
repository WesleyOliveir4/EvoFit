package com.example.evofit.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.evofit.domain.repository.SupportRepository

class SupportRepositoryImpl(
    private val context: Context
) : SupportRepository {
    override suspend fun sendSupportEmail(topic: String, message: String): Result<Unit> {
        return try {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("campossoftwarefactorybr@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, topic)
                putExtra(Intent.EXTRA_TEXT, message)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            if (emailIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(emailIntent)
                Result.success(Unit)
            } else {
                // Fallback: Tentativa sem resolveActivity (algumas versões do Android requerem queries no manifest)
                context.startActivity(emailIntent)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
