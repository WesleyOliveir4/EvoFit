package com.guaracode.evofit.presentation.ui.feature.profile.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.layout.ContentScale
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun ProfileImageComponent(
    profileImageUrl: String,
    contentDescription: String?,
    modifier: Modifier,
    contentScale: ContentScale
) {
    if (profileImageUrl.isNotEmpty()) {
        UIKitView(
            factory = {
                UIImageView().apply {
                    this.contentMode = when (contentScale) {
                        ContentScale.Crop -> UIViewContentMode.UIViewContentModeScaleAspectFill
                        else -> UIViewContentMode.UIViewContentModeScaleAspectFit
                    }
                    this.clipsToBounds = true
                }
            },
            update = { imageView ->
                val url = NSURL.URLWithString(profileImageUrl)
                if (url != null) {
                    val data = NSData.dataWithContentsOfURL(url)
                    if (data != null) {
                        imageView.image = UIImage.imageWithData(data)
                    }
                }
            },
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
