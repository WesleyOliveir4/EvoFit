package com.guaracode.evofit.presentation.ui.feature.profile.home.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

private const val TAG = "EvoFit_Profile"

class AndroidImagePickerLauncher(
    private val galleryLauncher: androidx.activity.result.ActivityResultLauncher<PickVisualMediaRequest>,
    private val cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>,
    private val getTempUri: () -> Uri?
) : ImagePickerLauncher {
    override fun launchGallery() {
        Log.d(TAG, "launchGallery called")
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun launchCamera() {
        Log.d(TAG, "launchCamera called")
        getTempUri()?.let { 
            Log.d(TAG, "launching camera with uri: $it")
            cameraLauncher.launch(it) 
        }
    }
}

@Composable
actual fun rememberImagePickerLauncher(onImageSelected: (String) -> Unit): ImagePickerLauncher {
    val context = LocalContext.current
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val saveUriToInternalStorage: (Uri) -> String? = { uri ->
        try {
            Log.d(TAG, "saveUriToInternalStorage started for uri: $uri")
            val inputStream = context.contentResolver.openInputStream(uri)
            val profileFile = File(context.filesDir, "profile_picture.jpg").apply {
                parentFile?.mkdirs()
            }
            if (inputStream != null) {
                FileOutputStream(profileFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                Log.d(TAG, "Image saved successfully to: ${profileFile.absolutePath}")
                profileFile.absolutePath
            } else {
                Log.e(TAG, "inputStream is null for uri: $uri")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving image to internal storage", e)
            null
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        Log.d(TAG, "galleryLauncher result uri: $uri")
        uri?.let {
            val localPath = saveUriToInternalStorage(it)
            Log.d(TAG, "galleryLauncher saved localPath: $localPath")
            if (localPath != null) {
                onImageSelected(localPath)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        logCameraResult(success, tempPhotoUri, saveUriToInternalStorage, onImageSelected)
    }

    return remember(galleryLauncher, cameraLauncher, context) {
        AndroidImagePickerLauncher(
            galleryLauncher = galleryLauncher,
            cameraLauncher = cameraLauncher,
            getTempUri = {
                val file = File(context.cacheDir, "images/profile_temp.jpg").apply {
                    parentFile?.mkdirs()
                }
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                tempPhotoUri = uri
                Log.d(TAG, "Generated temp camera uri: $uri")
                uri
            }
        )
    }
}

private fun logCameraResult(
    success: Boolean,
    tempPhotoUri: Uri?,
    saveUriToInternalStorage: (Uri) -> String?,
    onImageSelected: (String) -> Unit
) {
    Log.d(TAG, "cameraLauncher success: $success")
    if (success) {
        tempPhotoUri?.let { uri ->
            val localPath = saveUriToInternalStorage(uri)
            Log.d(TAG, "cameraLauncher saved localPath: $localPath")
            if (localPath != null) {
                onImageSelected(localPath)
            }
        }
    }
}
