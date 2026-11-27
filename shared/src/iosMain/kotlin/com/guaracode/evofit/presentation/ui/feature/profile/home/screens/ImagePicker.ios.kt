package com.guaracode.evofit.presentation.ui.feature.profile.home.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.*
import platform.Foundation.*
import platform.darwin.NSObject

private class ImagePickerDelegate(
    private val onImageSelected: (String) -> Unit,
    private val onDismiss: (UIImagePickerController) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

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
        onDismiss(picker)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        onDismiss(picker)
    }
}

class IosImagePickerLauncher(
    private val onImageSelected: (String) -> Unit
) : ImagePickerLauncher {

    private var delegate: ImagePickerDelegate? = null

    override fun launchGallery() {
        val picker = UIImagePickerController().apply {
            sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
            delegate = ImagePickerDelegate(onImageSelected) { p ->
                p.dismissViewControllerAnimated(true, completion = null)
                delegate = null
            }.also { delegate = it }
        }
        getRootViewController()?.presentViewController(picker, animated = true, completion = null)
    }

    override fun launchCamera() {
        if (UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)) {
            val picker = UIImagePickerController().apply {
                sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                delegate = ImagePickerDelegate(onImageSelected) { p ->
                    p.dismissViewControllerAnimated(true, completion = null)
                    delegate = null
                }.also { delegate = it }
            }
            getRootViewController()?.presentViewController(picker, animated = true, completion = null)
        }
    }

    private fun getRootViewController(): UIViewController? {
        val keyWindow = UIApplication.sharedApplication.keyWindow ?: UIApplication.sharedApplication.windows.firstOrNull { (it as? UIWindow)?.let { win -> win.isKeyWindow() } == true } as? UIWindow
        return keyWindow?.rootViewController
    }
}

@Composable
actual fun rememberImagePickerLauncher(onImageSelected: (String) -> Unit): ImagePickerLauncher {
    return remember { IosImagePickerLauncher(onImageSelected) }
}
