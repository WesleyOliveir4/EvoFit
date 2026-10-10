package com.guaracode.evofit.presentation.mapper

class AuthErrorMapper {
    fun map(throwable: Throwable): String {
        val message = throwable.message ?: ""
        return when {
            message.contains("weak-password", ignoreCase = true) -> "A senha informada não atende aos requisitos mínimos de segurança."
            message.contains("invalid-credential", ignoreCase = true) || 
            message.contains("user-not-found", ignoreCase = true) || 
            message.contains("wrong-password", ignoreCase = true) -> "E-mail ou senha incorretos. Verifique seus dados."
            message.contains("email-already-in-use", ignoreCase = true) -> "Não foi possível realizar o cadastro com os dados informados."
            message.contains("user-disabled", ignoreCase = true) -> "Esta conta foi desativada. Entre em contato com o suporte."
            message.contains("too-many-requests", ignoreCase = true) -> "Muitas tentativas malsucedidas. Tente novamente mais tarde."
            message.contains("network", ignoreCase = true) -> "Falha na conexão. Verifique sua internet."
            else -> "Ocorreu um erro inesperado. Tente novamente em instantes."
        }
    }
}
