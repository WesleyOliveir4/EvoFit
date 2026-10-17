package com.guaracode.evofit.domain.usecase

class GetAppVersionUseCaseImpl : GetAppVersionUseCase {
    override fun invoke(): String {
        return "1.0.0"
    }
}
