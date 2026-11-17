package com.guaracode.evofit.presentation.ui.feature.profile.home.screens

import androidx.compose.runtime.Composable

interface ImagePickerLauncher {
    fun launchGallery()
    fun launchCamera()
}

@Composable
expect fun rememberImagePickerLauncher(onImageSelected: (String) -> Unit): ImagePickerLauncher
