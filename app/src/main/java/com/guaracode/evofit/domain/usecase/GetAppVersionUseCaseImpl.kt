package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.BuildConfig

class GetAppVersionUseCaseImpl : GetAppVersionUseCase {
    override fun invoke(): String {
        return BuildConfig.VERSION_NAME
    }
}
