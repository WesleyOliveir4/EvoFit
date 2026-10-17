package com.guaracode.evofit.presentation.mapper

class AuthErrorMapper {
    fun map(throwable: Throwable): String {
        val msg = throwable.message ?: ""
        return when {
            msg.contains("invalid-email", true) || msg.contains("wrong-password", true) || msg.contains("user-not-found", true) -> "E-mail ou senha inválidos."
            msg.contains("email-already-in-use", true) -> "Este e-mail já está em uso."
            msg.contains("user-disabled", true) -> "Este usuário foi desativado."
            msg.contains("too-many-requests", true) -> "Muitas tentativas. Tente novamente mais tarde."
            msg.contains("weak-password", true) -> "A senha é muito fraca."
            else -> msg.ifBlank { "Ocorreu um erro inesperado." }
        }
    }
}
