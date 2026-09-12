# Plano de Implementação - Refatoração do GoalWizard para Screen

Este plano visa transformar o `GoalWizardBottomSheet` em uma `GoalWizardScreen` independente, seguindo os princípios CLEAN, SOLID e MVVM, e organizando-a em um novo pacote `commons/goals`.

## User Review Required

> [!IMPORTANT]
> A transformação em Screen implica que o Wizard deixará de ser um componente de sobreposição (BottomSheet) para ocupar a tela inteira ou ser um componente de navegação. Vou manter a lógica de "Wizard" mas centralizada em um `ViewModel`.

## Proposed Changes

### [Component Name] com.example.evofit.presentation.ui.feature.commons.goals

#### [NEW] [GoalWizardViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/commons/goals/viewmodel/GoalWizardViewModel.kt)
- Criar o `ViewModel` para gerenciar o estado do Wizard.
- Injetar `GetMuscleGroupsUseCase` e `GetExercisesByGroupUseCase`.
- Gerenciar `GoalWizardUiState` e processar `GoalAction`.

#### [NEW] [GoalWizardScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/commons/goals/screens/GoalWizardScreen.kt)
- Implementar a tela principal do Wizard utilizando o `GoalWizardViewModel`.
- Substituir o uso de `remember` e `mutableStateOf` pelo estado vindo do `ViewModel`.

#### [NEW] [GoalWizardComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/commons/goals/components/GoalWizardComponents.kt)
- Extrair os componentes visuais (Passos, Progress, TopBar) de `GoalWizard.kt` para este arquivo para melhor organização.

---

### [Onboarding/Profile] Limpeza e Integração

#### [MODIFY] [PersonalGoalsScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/profile/goals/screens/PersonalGoalsScreen.kt)
- Atualizar para usar a nova `GoalWizardScreen` ou um componente adaptado. (Manterei como um diálogo ou tela cheia dependendo da necessidade de fluxo).

#### [MODIFY] [OnboardingGoalsScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/onboard/screens/OnboardingGoalsScreen.kt)
- Atualizar para usar a nova implementação.

#### [DELETE] [GoalWizard.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/onboard/components/GoalWizard.kt)
- Remover o arquivo antigo após a migração completa.

## Verification Plan

### Automated Tests
- Validar a compilação e o correto funcionamento do fluxo de passos do Wizard.

### Manual Verification
- Testar a criação de metas via `PersonalGoalsScreen` e `OnboardingGoalsScreen`.
- Verificar se a navegação entre os passos (Tipo -> Grupo -> Exercício -> Valor) permanece correta.
