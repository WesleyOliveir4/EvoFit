package com.guaracode.evofit.domain.usecase

import com.guaracode.evofit.core.common.AppVersionConfig

class GetAppVersionUseCaseImpl : GetAppVersionUseCase {
    override fun invoke(): String {
        return AppVersionConfig.VERSION_NAME
    }
}
