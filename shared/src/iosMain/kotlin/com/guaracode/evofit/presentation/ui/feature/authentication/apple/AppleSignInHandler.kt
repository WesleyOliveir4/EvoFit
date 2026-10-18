package com.guaracode.evofit.presentation.ui.feature.authentication.apple

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AuthenticationServices.*
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume

class AppleSignInHandler {
    suspend fun signIn(): Result<Unit> = suspendCancellableCoroutine { continuation ->
        val provider = ASAuthorizationAppleIDProvider()
        val request = provider.createRequest().apply {
            requestedScopes = listOf(ASAuthorizationScopeEmail, ASAuthorizationScopeFullName)
        }

        val controller = ASAuthorizationController(listOf(request))
        
        val delegate = object : NSObject(), ASAuthorizationControllerDelegateProtocol {
            override fun authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization: ASAuthorization) {
                val credential = didCompleteWithAuthorization.credential as? ASAuthorizationAppleIDCredential
                if (credential != null && credential.identityToken != null) {
                    continuation.resume(Result.success(Unit))
                    return
                }
                continuation.resume(Result.failure(Exception("Apple Sign-In failed")))
            }

            override fun authorizationController(controller: ASAuthorizationController, didCompleteWithError: NSError) {
                continuation.resume(Result.failure(Exception(didCompleteWithError.localizedDescription)))
            }
        }

        val presentationProvider = object : NSObject(), ASAuthorizationControllerPresentationContextProvidingProtocol {
            override fun presentationAnchorForAuthorizationController(controller: ASAuthorizationController): UIWindow {
                return UIApplication.sharedApplication.keyWindow ?: UIWindow()
            }
        }

        controller.delegate = delegate
        controller.presentationContextProvider = presentationProvider
        controller.performRequests()
    }
}
