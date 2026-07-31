# Otimização de Estrutura e Scroll nas Telas de Autenticação

Este plano visa corrigir a sensação de "scroll desnecessário" nas telas de autenticação, movendo os rodapés (footers) para slots fixos do `Scaffold` e otimizando a área rolável.

## User Review Required

> [!IMPORTANT]
> A mudança principal consiste em usar o slot `bottomBar` do `Scaffold`. Isso fará com que o botão principal e o link de navegação fiquem sempre visíveis e fixos na base da tela, enquanto o formulário ocupará o espaço central, rolando apenas se necessário (ex: teclado aberto ou tela pequena).

## Proposed Changes

### Authentication Feature - Screens

#### [MODIFY] [RegisterScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/screens/RegisterScreen.kt)
- Mover `RegisterFooter` para `bottomBar`.
- Remover `verticalScroll` da Column principal.
- Envolver Header e Formulário em uma Column com `Modifier.weight(1f).verticalScroll()` para que o scroll só ocorra no conteúdo central se ele transbordar.

#### [MODIFY] [NewPasswordScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/screens/NewPasswordScreen.kt)
- Seguir o mesmo padrão: Footer fixo no `bottomBar`, conteúdo central rolável apenas se necessário.

#### [MODIFY] [VerifyCodeScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/screens/VerifyCodeScreen.kt)
- Ajustar estrutura para garantir que o teclado não cubra o botão de verificar.

#### [MODIFY] [ForgotPasswordScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/screens/ForgotPasswordScreen.kt) e [RecoverPasswordScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/authentication/screens/RecoverPasswordScreen.kt)
- Aplicar a padronização de rodapé fixo.

## Verification Plan

### Manual Verification
- Verificar se em telas grandes (Emulator Pixel 4+) o scroll desapareceu.
- Testar a abertura do teclado e garantir que o formulário "sobe" corretamente e se torna rolável.
- Validar se o rodapé permanece fixo na base da tela sem sobrepor o conteúdo.
