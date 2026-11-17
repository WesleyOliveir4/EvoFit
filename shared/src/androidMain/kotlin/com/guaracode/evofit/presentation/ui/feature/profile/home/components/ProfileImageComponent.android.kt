package com.guaracode.evofit.presentation.ui.feature.profile.home.components

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import java.io.File

private const val TAG = "EvoFit_Profile"

@Composable
actual fun ProfileImageComponent(
    profileImageUrl: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale
) {
    Log.d(TAG, "ProfileImageComponent rendered with profileImageUrl: '$profileImageUrl'")
    if (profileImageUrl.isNotEmpty()) {
        val file = File(profileImageUrl)
        val exists = file.exists()
        Log.d(TAG, "File path: $profileImageUrl, exists: $exists")
        val model: Any = if (profileImageUrl.startsWith("/")) {
            file
        } else {
            profileImageUrl
        }
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
        )
    }
}
