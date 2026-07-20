# Plano — Novo Fluxo de Autenticação EvoFit

Documento de planejamento para evolução do fluxo de autenticação do app, substituindo a entrada direta em "Login" por um fluxo completo de **Pré-Login → Login/Cadastro → Recuperação de Senha**, mantendo Firebase como provedor de identidade e preservando o gate de Onboarding já existente.

---

## 0. Contexto atual (levantado no código)

- Arquitetura em **Clean Architecture + MVVM**: `data / domain / presentation`, com `presentation/ui/feature/<feature>/{screens, components, state, viewmodel}`.
- Padrão de tela: `XyzScreen` (stateful, injeta `ViewModel` via Koin) → coleta `uiState` → repassa para `XyzContent` (stateless, `@Preview`-friendly) → que usa componentes de `components/XyzComponents.kt`.
- Injeção de dependências via **Koin** (`di/AppModule.kt`), separada em `dataModule`, `domainModule`, módulos de feature (ex.: `authModule`) e agregada em `appModule`.
- Casos de uso seguem o padrão `interface XyzUseCase { suspend operator fun invoke(...): Result<T> }` + `class XyzUseCaseImpl(...) : XyzUseCase`.
- **Firebase Auth** já é usado para e-mail/senha via `AuthRepository` → `AuthRepositoryImpl(FirebaseAuth)`. `FirebaseFirestore` também já está no projeto.
- Navegação: `NavRoutes` (sealed class de rotas) + `NavNavigation.kt` (NavHost único). Hoje: `Splash → Login/Register → Onboarding → Home`.
- Gate de onboarding já existe: `SplashViewModel` decide o destino inicial usando `IsUserLoggedInUseCase` + `IsOnboardingCompletedUseCase`; `LoginViewModel` também resolve `isOnboardingCompleted` após login para decidir se navega para `Home` ou `Onboarding`.
- Tema: cores `AppDarkBg`, `AppGreen`, `AppSurface`, `TextPrimary/Secondary`, componente `LoginInputField` reutilizável, botões `RoundedCornerShape(28.dp)`.
- Não há hoje: tela de pré-login/boas-vindas, tela de "esqueci senha" (o clique existe mas não navega para nada), login social (Google/Apple), Credential Manager / Google Sign-In SDK nas dependências.

---

## 1. Objetivo

Implementar o fluxo abaixo, chamado no lugar da tela de Login atual como ponto de entrada pós-Splash (quando o usuário não está logado), reaproveitando o gate de onboarding já existente após o login (convencional ou após redefinição de senha):

1. **Pré-Login** (Boas-vindas) — tela 1 do mock.
2. **Login** — tela 2 (e-mail/senha + Google + Apple).
3. **Criar Conta** — tela 3 (nome, e-mail, senha, confirmar senha, termos).
4. **Esqueci minha senha** (intro) — tela 4.
5. **Recuperar senha** (inserir e-mail) — tela 5.
6. **Verificar e-mail** (código de 6 dígitos) — tela 6.
7. **Criar nova senha** — tela 7 (não estava no mock, será desenhada seguindo o mesmo padrão visual).

Após o passo 7 (ou login convencional / social), o app deve seguir a regra **já existente**: se o onboarding não foi concluído → `Onboarding`; caso contrário → `Home`.

---

## 2. Arquitetura da solução (por camada)

### 2.1 Presentation (UI) — `presentation/ui/feature/authentication/`
```
authentication/
├── screens/
│   ├── PreLoginScreen.kt          (novo)
│   ├── LoginScreen.kt             (evoluir: + Google/Apple)
│   ├── RegisterScreen.kt          (evoluir: + Nome, Confirmar senha, Termos)
│   ├── ForgotPasswordScreen.kt    (novo — tela 4, intro)
│   ├── RecoverPasswordScreen.kt   (novo — tela 5, inserir e-mail)
│   ├── VerifyCodeScreen.kt        (novo — tela 6, código de 6 dígitos)
│   └── NewPasswordScreen.kt       (novo — tela 7, nova senha)
├── components/
│   ├── PreLoginComponents.kt      (novo)
│   ├── LoginComponents.kt         (evoluir: + SocialLoginButtons/Divider)
│   ├── RegisterComponents.kt      (evoluir: + Termos, campos extras)
│   ├── ForgotPasswordComponents.kt(novo)
│   ├── RecoverPasswordComponents.kt(novo)
│   ├── VerifyCodeComponents.kt    (novo — OTP de 6 caixas)
│   └── NewPasswordComponents.kt   (novo)
├── state/
│   └── *UiState.kt (um por tela, ver seção 2.2)
└── viewmodel/
    └── *ViewModel.kt (um por tela, ver seção 2.2)
```
Mantém o padrão existente: `Screen` (stateful) injeta `ViewModel` via `koinViewModel()`, observa `StateFlow<UiState>`, delega toda a lógica de apresentação para uma função `XyzContent` **stateless** e reutilizável em `@Preview`.

### 2.2 Domain — novos casos de uso (`domain/usecase/`)
Seguindo o padrão `interface + Impl` já usado:

| Caso de uso | Responsabilidade |
|---|---|
| `LoginUseCase` *(existe)* | login e-mail/senha |
| `RegisterUseCase` *(existe)* | cadastro e-mail/senha |
| `LoginWithGoogleUseCase` | login/cadastro via Google (Firebase `GoogleAuthProvider`) |
| `LoginWithAppleUseCase` | login/cadastro via Apple (Firebase `OAuthProvider("apple.com")`) |
| `SendPasswordResetCodeUseCase` | dispara o fluxo de recuperação (Firebase `sendPasswordResetEmail` **ou** Firebase Auth email-link / código customizado — ver Nota Firebase abaixo) |
| `VerifyPasswordResetCodeUseCase` | valida o código de 6 dígitos informado pelo usuário |
| `ConfirmPasswordResetUseCase` | efetiva a troca de senha (`FirebaseAuth.confirmPasswordReset`) |
| `IsOnboardingCompletedUseCase` *(existe)* | reaproveitado após login/reset |

> **Nota Firebase — código de 6 dígitos:** O Firebase Auth nativo (`sendPasswordResetEmail`) envia um **link** de redefinição, não um código numérico de 6 dígitos como no mock (tela 6). Para manter fidelidade total ao design, a opção recomendada é usar o **Firebase Auth Email Link / `oobCode`** por trás dos panos (o "código" pode ser os 6 últimos dígitos do link gerado, entregues via **Firebase Cloud Function + Firebase Extensions "Trigger Email"**, ou via **Firebase Auth `ActionCodeSettings`** interceptando o `oobCode`). Alternativa mais simples e 100% client-side: usar `sendPasswordResetEmail` normalmente e adaptar a Tela 6 para "verificar se o link foi aberto" via `checkActionCode`. Essa decisão de implementação será tratada no Passo 5 (Integração Firebase) — não bloqueia a construção das telas (Passo 1).

### 2.3 Data — `data/repository/AuthRepositoryImpl.kt`
Expandir `AuthRepository` (interface em `domain/repository`) com:
```kotlin
interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun loginWithGoogle(idToken: String): Result<Unit>
    suspend fun loginWithApple(): Result<Unit>
    suspend fun sendPasswordResetCode(email: String): Result<Unit>
    suspend fun verifyPasswordResetCode(email: String, code: String): Result<String> // retorna oobCode
    suspend fun confirmPasswordReset(oobCode: String, newPassword: String): Result<Unit>
    fun isLoggedIn(): Boolean
}
```
Implementação usa `FirebaseAuth` (já injetado via Koin) + `GoogleSignIn`/`Credential Manager` para Google e `OAuthProvider` para Apple.

### 2.4 DI — `di/AppModule.kt`
- Adicionar ao `dataModule`: nada novo além do já existente `FirebaseAuth` (reaproveitado).
- Adicionar ao `domainModule`: os novos `factory<...UseCase>`.
- Expandir `authModule` com os novos ViewModels (`PreLoginViewModel` pode nem ser necessário — tela estática; `ForgotPasswordViewModel`, `RecoverPasswordViewModel`, `VerifyCodeViewModel`, `NewPasswordViewModel`, e evoluir `LoginViewModel`/`RegisterViewModel`).

### 2.5 Navegação — `navigation/NavRoutes.kt` + `NavNavigation.kt`
Novas rotas:
```kotlin
object PreLogin : NavRoutes("pre_login")
object ForgotPassword : NavRoutes("forgot_password")
object RecoverPassword : NavRoutes("recover_password")
object VerifyCode : NavRoutes("verify_code?email={email}") { fun createRoute(email: String) = "verify_code?email=$email" }
object NewPassword : NavRoutes("new_password?oobCode={oobCode}") { fun createRoute(oobCode: String) = "new_password?oobCode=$oobCode" }
```
- `SplashViewModel` passa a apontar para `PreLogin` (em vez de `Login`) quando `!isLoggedIn`.
- `PreLoginScreen` → `Começar` → `Login`.
- `LoginScreen` → `onForgotPasswordClick` → `ForgotPassword` (hoje é um TODO vazio).
- `ForgotPasswordScreen` → `Continuar` → `RecoverPassword`.
- `RecoverPasswordScreen` → `Enviar código` → `VerifyCode(email)`.
- `VerifyCodeScreen` → `Verificar código` → `NewPassword(oobCode)`.
- `NewPasswordScreen` → `Salvar` → volta para `Login` (`popUpTo` limpando toda a pilha de recuperação), com mensagem de sucesso.
- Login com sucesso (convencional, Google ou Apple) e término do fluxo de nova senha **continuam usando a regra já existente**: `isOnboardingCompleted ? Home : Onboarding`.

### 2.6 Firebase (requisito 4)
- **Login e-mail/senha**: `FirebaseAuth.signInWithEmailAndPassword` *(já implementado)*.
- **Cadastro**: `FirebaseAuth.createUserWithEmailAndPassword` + `updateProfile(displayName)` para gravar o campo "Nome" novo do formulário *(evoluir o existente)*.
- **Login Google**: `Credential Manager` (`androidx.credentials`) + `GoogleIdTokenCredential` → `GoogleAuthProvider.getCredential(idToken, null)` → `FirebaseAuth.signInWithCredential`. Exige novas dependências (ver seção 4).
- **Login Apple**: `OAuthProvider.newBuilder("apple.com")` + `FirebaseAuth.startActivityForSignInWithProvider` (Apple Sign-In no Android é feito 100% via Firebase OAuth genérico, sem SDK extra da Apple).
- **Esqueci senha**: `FirebaseAuth.sendPasswordResetEmail` / `confirmPasswordReset` (ver Nota Firebase acima para o código de 6 dígitos).

---

## 3. Consistência visual (Design System já existente)

Todas as novas telas reaproveitam o que já existe, sem criar tokens novos:
- Cores: `AppDarkBg` (fundo), `AppGreen` (destaque/CTA), `AppSurface` (inputs), `TextPrimary/TextSecondary`.
- Componente de campo: `LoginInputField` (reutilizado em todas as telas com campo de texto).
- Botão primário: `Button` verde, `RoundedCornerShape(28.dp)`, altura 56.dp, texto bold — mesmo padrão de `LoginFooter`/`RegisterFooter`.
- Cabeçalhos: título grande (`32.sp`, `FontWeight.Black`, `AppGreen`) + subtítulo (`TextSecondary`), como em `LoginHeader`/`RegisterHeader`.
- `Scaffold(containerColor = AppDarkBg)` + `Column` com `verticalScroll` + `padding(horizontal = 24.dp)`.

---

## 4. Dependências novas necessárias (Gradle)

Para viabilizar o Passo 5 (Firebase completo), serão necessárias no `libs.versions.toml` / `app/build.gradle.kts`:
- `androidx.credentials:credentials` + `androidx.credentials:credentials-play-services-auth`
- `com.google.android.libraries.identity.googleid:googleid`
- (Apple não exige lib extra — usa `FirebaseAuth` + `OAuthProvider`)

> Isso **não** é necessário para o Passo 1 (construção das telas), apenas para o Passo 5 (integração real dos botões sociais).

---

## 5. Passos de execução

| # | Passo | Entregável |
|---|---|---|
| **1** | **Construir as novas telas de UI** (screens + components), seguindo o padrão MVVM/Clean/SOLID já estabelecido, com estado local simples (sem regra de negócio ainda) para permitir preview/navegação isolada. | Código-fonte das telas + `strings.xml` atualizado |
| 2 | Criar `state/` e `viewmodel/` completos por tela, com `UiState` imutável e funções de intenção (`onXChange`, `onXClick`), ligando às telas do Passo 1 | ViewModels + Koin `authModule` atualizado |
| 3 | Expandir `domain` (`AuthRepository` + novos UseCases) e `data` (`AuthRepositoryImpl`) para Google, Apple e recuperação de senha | Casos de uso + repositório |
| 4 | Adicionar rotas (`NavRoutes`) e ligar tudo em `NavNavigation.kt`, atualizando `SplashViewModel` para iniciar em `PreLogin` | Navegação completa |
| 5 | Integração Firebase real: Credential Manager (Google), `OAuthProvider` (Apple), fluxo de reset de senha com código de 6 dígitos | Dependências Gradle + configuração Firebase Console (OAuth client IDs) |
| 6 | Testes unitários dos novos UseCases e ViewModels (seguindo `GetEvoHomeSummaryUseCaseTest.kt` como referência) | Testes em `app/src/test` |
| 7 | QA visual (comparação pixel-a-pixel com o mock) + testes manuais dos fluxos de erro (código inválido, e-mail inexistente, senha fraca, cancelamento do login social) | Checklist de QA |

**Este documento entrega o Passo 1** (telas construídas) empacotado junto com este plano em `.zip`.

---

## 6. Riscos e decisões em aberto

- **Código de 6 dígitos vs. link do Firebase**: decisão de implementação (ver Nota Firebase, seção 2.2) a ser fechada no Passo 5.
- **Apple Sign-In requer configuração no Firebase Console** (Service ID, chave privada, domínio de retorno) antes de funcionar em produção — depende de acesso ao console do projeto.
- **Campo "Nome" no cadastro**: hoje `RegisterUseCase`/`AuthRepository` não persistem nome; será necessário decidir se vai para `FirebaseAuth.updateProfile` e/ou documento em `Firestore` (há um `UserRemoteDataSource` que já grava dados de usuário — reaproveitar).

---

## 7. Status desta entrega (Passo 1 concluído)

Os arquivos abaixo foram criados/alterados nesta entrega, já seguindo a separação `screens/` + `components/` e o padrão visual do Design System do EvoFit:

**Novos:**
- `screens/PreLoginScreen.kt` + `components/PreLoginComponents.kt`
- `screens/ForgotPasswordScreen.kt` + `components/ForgotPasswordComponents.kt`
- `screens/RecoverPasswordScreen.kt` + `components/RecoverPasswordComponents.kt`
- `screens/VerifyCodeScreen.kt` + `components/VerifyCodeComponents.kt`
- `screens/NewPasswordScreen.kt` + `components/NewPasswordComponents.kt`
- `res/drawable/ic_google.xml`, `res/drawable/ic_apple.xml`

**Evoluídos:**
- `screens/LoginScreen.kt` + `components/LoginComponents.kt` → adicionado divisor "ou continue com" e botões sociais Google/Apple (`LoginSocialDivider`, `SocialLoginButtons`), com callbacks `onGoogleClick`/`onAppleClick` já expostos na assinatura de `LoginScreen`.
- `screens/RegisterScreen.kt` + `components/RegisterComponents.kt` → adicionados campos "Nome" e "Confirmar senha", validação de senhas coincidentes e checkbox de Termos de Uso/Política de Privacidade (`TermsCheckboxRow`).
- `res/values/strings.xml` → todas as strings pt-BR das telas novas/alteradas.

**Importante:** por ser o Passo 1 (construção de UI), as telas novas (Pré-Login, Esqueci senha, Recuperar senha, Verificar código, Nova senha) usam **estado local (`remember`)** em vez de `ViewModel`/Koin — isso mantém o app compilando sem exigir ainda os UseCases/Firebase do Passo 2 a 5, e será substituído por `ViewModel + StateFlow` (mesmo padrão de `LoginViewModel`) sem alterar a assinatura dos `*Content` composables. `LoginScreen`/`RegisterScreen` continuam usando seus `ViewModel`s reais (já integrados ao Firebase); os novos campos de `RegisterScreen` (nome, confirmar senha, termos) também estão como estado local até o Passo 2, quando serão movidos para `RegisterViewModel`/`RegisterUiState`.

Nenhuma rota de navegação (`NavRoutes`/`NavNavigation.kt`), `ViewModel`, `UseCase`, `Repository` ou dependência Gradle foi alterada nesta entrega — isso está propositalmente reservado para os Passos 2 a 5 descritos acima, para manter o build atual estável enquanto as telas são revisadas.
