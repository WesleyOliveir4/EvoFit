# Refatoração do Módulo Workout para Design System

Este plano detalha a migração de todos os componentes e telas do módulo de treinos (`workout`) para o novo Design System, utilizando dimensões centralizadas, tipografia semântica e cores dinâmicas via `MaterialTheme`.

## User Review Required

> [!IMPORTANT]
> - **Cores:** Cores diretas como `AppGreen` serão substituídas por `MaterialTheme.colorScheme.primary`.
> - **Estrutura:** Botões de ação em telas de formulário/fluxo (como "Continuar" ou "Iniciar Treino") serão movidos para o slot `bottomBar` do `Scaffold` com padding lateral padronizado de 16dp.
> - **Scroll:** Todas as telas roláveis serão ajustadas para começar no topo e utilizar o sistema de scroll inteligente.

## Proposed Changes

### 1. Workout Home (Treinos & Histórico)
Refatoração da tela principal e seus componentes de lista.

- **Files:** `WorkoutScreen.kt`, `WorkoutListItem.kt`, `ActiveWorkoutCard.kt`, `WorkoutDoneItem.kt`, `StatsSection.kt`, `TrainingHeader.kt`, `WorkoutSegmentedControl.kt`, `OfflineToast.kt`, `WorkoutEmptyState.kt`.
- **Mudanças:**
    - Substituição de `16.dp` por `Dimens.ScreenPaddingHorizontal`.
    - Substituição de `24.dp`/`32.dp` por `Dimens.SectionSpacing` ou `Dimens.SpacingLarge`.
    - Uso de `MaterialTheme.typography` (headlineLarge, bodyLarge, bodySmall) em todos os textos.
    - Sincronização de cores de botões e ícones com o `colorScheme`.

### 2. Workout Execution (Execução do Treino)
Refatoração da visualização prévia e da tela de execução ativa.

- **Files:** `WorkoutPreviewScreen.kt`, `WorkoutStartScreen.kt`, `StartWorkoutComponents.kt`.
- **Mudanças:**
    - Mover botão "Iniciar treino" para o `bottomBar` do `Scaffold`.
    - Padronizar as caixas de seleção (checkboxes circulares) e indicadores de progresso.
    - Garantir que o contador de tempo e progresso use as cores do tema.

### 3. Workout Creation (Criação & Configuração)
Refatoração do fluxo de criação de novas fichas.

- **Files:** `NewWorkoutScreen.kt`, `SelectExercisesScreen.kt`, `ConfigureWorkoutScreen.kt`, `CreateWorkoutComponents.kt`, `ExerciseSelectionComponents.kt`, `WeightWheel.kt`, `SetInputComponents.kt`, `WeightPickerComponents.kt`.
- **Mudanças:**
    - Padronizar o "Weight Wheel Picker" com tipografia de exibição grande.
    - Fixar os botões "Próximo" e "Finalizar" na base da tela.
    - Sincronizar os cards de grupos musculares e linhas de exercícios com o tema.

### 4. Workout Resume (Resumo Final)
Refatoração da tela de sucesso/conclusão.

- **Files:** `WorkoutResumeScreen.kt`, `WorkoutResumeComponents.kt`.
- **Mudanças:**
    - Padronizar o card de resumo e os itens de linha.
    - Uso de `MaterialTheme.colorScheme.primaryContainer` para fundos de ícones de destaque.

## Verification Plan

### Automated Tests
- N/A (Foco em UI)

### Manual Verification
- Iniciar o app e navegar por: Home -> Criar Treino -> Selecionar Exercícios -> Configurar Séries -> Visualizar Treino -> Executar Treino -> Finalizar Treino.
- Validar se todas as transições visuais mantêm as cores e espaçamentos consistentes.
- Testar comportamento com teclado aberto em `ConfigureWorkoutScreen`.
