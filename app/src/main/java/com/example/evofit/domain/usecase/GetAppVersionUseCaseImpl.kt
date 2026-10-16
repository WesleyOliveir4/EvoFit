package com.example.evofit.domain.usecase

import com.example.evofit.BuildConfig

class GetAppVersionUseCaseImpl : GetAppVersionUseCase {
    override fun invoke(): String {
        return BuildConfig.VERSION_NAME
    }
}
