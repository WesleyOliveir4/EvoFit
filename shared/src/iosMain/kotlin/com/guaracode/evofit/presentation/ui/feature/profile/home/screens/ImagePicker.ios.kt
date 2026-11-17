package com.guaracode.evofit.presentation.ui.feature.profile.home.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.*
import platform.Foundation.*
import platform.darwin.NSObject

class IosImagePickerLauncher(
    private val onImageSelected: (String) -> Unit
) : ImagePickerLauncher, NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun launchGallery() {
        val picker = UIImagePickerController().apply {
            sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            delegate = this@IosImagePickerLauncher
        }
        getRootViewController()?.presentViewController(picker, animated = true, completion = null)
    }

    override fun launchCamera() {
        if (UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)) {
            val picker = UIImagePickerController().apply {
                sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                delegate = this@IosImagePickerLauncher
            }
            getRootViewController()?.presentViewController(picker, animated = true, completion = null)
        }
    }

    private fun getRootViewController(): UIViewController? {
        val keyWindow = UIApplication.sharedApplication.keyWindow ?: UIApplication.sharedApplication.windows.firstOrNull { (it as? UIWindow)?.let { win -> win.isKeyWindow() } == true } as? UIWindow
        return keyWindow?.rootViewController
    }

    override fun imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo: Map<Any?, *>) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        if (image != null) {
            val data = UIImageJPEGRepresentation(image, 0.8)
            val documentsDirectory = NSFileManager.defaultManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask).first() as? NSURL
            val fileName = "profile_${NSUUID().UUIDString}.jpg"
            val fileURL = documentsDirectory?.URLByAppendingPathComponent(fileName)
            if (fileURL != null && data != null) {
                data.writeToURL(fileURL, true)
                onImageSelected(fileURL.absoluteString ?: "")
            }
        }
        picker.dismissViewControllerAnimated(true, completion = null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
    }
}

@Composable
actual fun rememberImagePickerLauncher(onImageSelected: (String) -> Unit): ImagePickerLauncher {
    return remember { IosImagePickerLauncher(onImageSelected) }
}
