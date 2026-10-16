package com.guaracode.evofit.presentation

import platform.UIKit.UIViewController
import platform.UIKit.UILabel
import platform.UIKit.UIColor

fun MainViewController(): UIViewController {
    val viewController = UIViewController()
    val label = UILabel().apply {
        text = "EvoFit Shared"
        textColor = UIColor.blackColor
    }
    viewController.view.addSubview(label)
    return viewController
}
