# Padronização de TopBars via GlobalComponents

Este plano detalha a unificação das barras superiores (TopBars) nos pacotes `workout` e `evo` utilizando o componente centralizado `TopBarReturn`.

## Proposed Changes

### Global Components

#### [MODIFY] [GlobalComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/components/GlobalComponents.kt)
- **Flexibilidade:** Upgrade no componente `TopBarReturn` para suportar:
    - `subtitle: String?`: Para exibir informações secundárias abaixo do título.
    - `isCenterAligned: Boolean`: Para alternar entre `CenterAlignedTopAppBar` e `TopAppBar` (padrão Material).
    - `showBackIcon: Boolean`: Para telas de nível raiz (como Home) que não precisam de botão voltar.
    - `actions: @Composable RowScope.() -> Unit`: Para suportar botões de ação (Editar, Excluir, Filtros).
- **Consistência:** Uso de `MaterialTheme.typography.titleLarge` como padrão para títulos.

### Refatoração de Telas (Workout)

#### [MODIFY] `NewWorkoutScreen.kt`, `SelectExercisesScreen.kt`, `ConfigureWorkoutScreen.kt`, `WorkoutPreviewScreen.kt`
- Substituição de `TopAppBar` manual por `TopBarReturn`.
- Sincronização de cores e elevações (shadows) via Design System.

### Refatoração de Telas (Evo)

#### [MODIFY] `EvoHomeScreen.kt`, `MuscleGroupSelectionScreen.kt`, `ExerciseSelectionScreen.kt`, `ExerciseDetailAnalyticsScreen.kt`
- Unificação das barras de topo.
- Na `EvoHomeScreen`, o filtro de período foi movido para o slot de `actions` do `TopBarReturn`.
- Nas telas de análise, subtítulos como "Análise de evolução" foram integrados nativamente.

## Verification Plan

### Automated Tests
- Build do projeto executado com sucesso via Gradle.

### Manual Verification
- Navegar por todas as telas mencionadas e validar:
    - Alinhamento do título (Esquerda vs Centro).
    - Presença/Ausência do botão voltar.
    - Visibilidade de subtítulos.
    - Funcionamento dos botões de ação (Editar/Filtro).
