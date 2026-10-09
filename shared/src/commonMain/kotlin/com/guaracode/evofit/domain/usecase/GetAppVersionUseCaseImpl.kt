package com.guaracode.evofit.domain.usecase

class GetAppVersionUseCaseImpl(
    private val appVersion: String = "1.0.0"
) : GetAppVersionUseCase {
    override fun invoke(): String {
        return appVersion
    }
}
