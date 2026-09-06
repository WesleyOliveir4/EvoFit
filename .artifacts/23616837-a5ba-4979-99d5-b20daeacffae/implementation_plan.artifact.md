# Refatoração de Grupos Musculares e Exercícios

O objetivo é dividir o grupo "Braços" em três grupos distintos (Biceps, Triceps e Antebraço) no `LocalExerciseDataSource.kt`, reordenar todos os IDs e MuscleGroupIds para uma base limpa, e ajustar as referências em todo o app.

## User Review Required

> [!IMPORTANT]
> A reordenação de IDs afetará qualquer dado persistido localmente no dispositivo (se houver cache em banco de dados local ou SharedPreferences baseado em IDs fixos). Como o usuário mencionou que limpou a base de HK e PROD, assumimos que não há risco de inconsistência de dados legados para o usuário final.

## Proposed Changes

### [Component] Modelos e Enums

#### [MODIFY] [ExerciseModels.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/model/ExerciseModels.kt)
- Adicionar `BICEPS`, `TRICEPS` e `FOREARMS` ao enum `MuscleGroupType`.
- Remover `ARMS`.

#### [MODIFY] [MuscleGroup.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/domain/model/MuscleGroup.kt)
- Refletir as mudanças do enum no domínio.

---

### [Component] Mapeamento

#### [MODIFY] [ExerciseMapper.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/mapper/ExerciseMapper.kt)
- Atualizar o `toDomain()` e `toData()` para incluir os novos tipos de grupos musculares.

#### [MODIFY] [MuscleGroupMapper.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/mapper/MuscleGroupMapper.kt)
- Mapear os novos tipos para ícones/recursos de imagem correspondentes.

---

### [Component] Dados

#### [MODIFY] [LocalExerciseDataSource.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/datasource/LocalExerciseDataSource.kt)
- Remover o grupo "Braços" (ID 4).
- Adicionar "Biceps", "Triceps" e "Antebraço".
- Reordenar todos os IDs de `MuscleGroupModel` de 1 a 11.
- Redistribuir os exercícios de Braços nos novos grupos.
- Reordenar todos os IDs de `ExerciseModel`.
- Atualizar as sugestões de metas (`suggestions`) com os novos IDs.

---

### [Component] UI e Ajustes de IDs Hardcoded

#### [MODIFY] Vários arquivos de UI
- Atualizar referências a IDs e tipos hardcoded em Previews e Mock Data para garantir que a UI continue exibindo dados corretos.
- Arquivos afetados incluem: `GoalWizard.kt`, `MuscleGroupSelectionScreen.kt`, `NewWorkoutScreen.kt`, `SelectExercisesScreen.kt`, entre outros identificados no grep.

---

### [Component] Sugestões de Novos Treinos

#### [NEW] [WorkoutSuggestions.artifact.md](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/.artifacts/23616837-a5ba-4979-99d5-b20daeacffae/WorkoutSuggestions.artifact.md)
- Propor novas rotinas de treino aproveitando a divisão mais granular (ex: Treino de Biceps e Antebraço, Treino de Triceps e Peito).

## Verification Plan

### Automated Tests
- Executar testes unitários existentes para garantir que o mapeamento e a lógica de busca de exercícios por grupo ainda funcionam.
- `gradlew test`

### Manual Verification
- Abrir a tela de seleção de exercícios no app e verificar se os novos grupos aparecem corretamente.
- Verificar se os ícones dos novos grupos estão sendo exibidos.
- Validar se ao selecionar um grupo (ex: Biceps), apenas os exercícios corretos são listados.
