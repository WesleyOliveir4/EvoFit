# EvoFit — Status da Refatoração e Próximos Passos

## ✅ Concluído (validado por revisão manual, sem compilação — ver aviso abaixo)

### Fase 1 — Padronização de UiModel

* Presentation não conhece mais `domain.model` em nenhuma tela/estado, exceto duas exceções documentadas:

  1. Enums simples sem lógica própria (`MeasurementUnit`, `EvoPeriod`, `MuscleGroupType`) passam direto.
  2. `NewGoalDialog` (usado em Onboarding e Profile Goals) continua construindo `UserGoal`/consumindo `MuscleGroup`/`Exercise`/`GoalSuggestion` — wizard de criação isolado, não reescrito por risco/escopo.
* `WorkoutUIModel` não carrega mais `ImageVector` (ViewModel não decide mais ícone de Compose).
* Novos UiModels criados: `GoalUIModel`, `StrengthGainUIModel`, `MuscleEvolutionUIModel`, `ExerciseWithRecordsUIModel`, `WorkoutHistoryUIModel`, `ActiveSessionUIModel`.

### Fase 2 — Corrigir violação Data → Presentation

* `DateMapper` movido de `presentation/mapper` para `core/common` (local neutro).
* Lógica de filtro por período extraída do `WorkoutRepositoryImpl` para `FilterWorkoutHistoryByPeriodUseCase`.
* `WorkoutRepository` só tem `getWorkoutDoneHistory(userId)` — sem overload com período.

### Fase 3 — UseCases com Interface + Impl

* `GetExerciseDataUseCase` (fachada de 4 responsabilidades) quebrado em `GetMuscleGroupsUseCase`, `GetExercisesByGroupUseCase`, `GetExercisesByIdsUseCase`, `GetGoalSuggestionsUseCase`.
* `RegisterUseCase`, `LoginUseCase`, `SaveWorkoutDoneUseCase`, `GetWorkoutDoneHistoryUseCase`, `GetWorkoutDoneByIdUseCase`, `GetCurrentWeekRangeUseCase`, `GetActiveUserGoalsUseCase`, `CalculateGoalProgressUseCase` — todos com interface agora.
* `AppModule.kt` (Koin) e todos os ViewModels consumidores atualizados.
* Bug pré-existente corrigido: o único teste do projeto (`GetEvoHomeSummaryUseCaseTest`) não compilava (construtor com aridade errada).

## ⚠️ Aviso importante

Nada disso foi compilado — o sandbox não tem acesso à internet para baixar o Gradle/toolchain Kotlin. Toda a validação foi por leitura manual + greps de sanidade (busquei especificamente por referências quebradas, imports não usados, e overloads/assinaturas divergentes). **Rode um build local antes de seguir para a Fase 4.**

\---

## 🔜 Fase 4 — Migração de IDs para UUID (não iniciada)

Essa fase é estruturalmente diferente das anteriores: é uma mudança **vertical** (corta todas as camadas de uma vez), não isolável por feature. Migrar pela metade deixa o projeto sem compilar — por isso não comecei sem conseguir terminar em uma sessão só.

### Escopo levantado (grep no projeto):

* **Entities/Room:** `WorkoutEntity.workoutId`, `WorkoutExerciseEntity.id` + FK, `ExerciseSetEntity.id` + FK — todos `Long autoGenerate` → `String (UUID)`.
* **Migration do Room:** `AppDatabase` versão atual → +1, com `Migration` explícita (não usar `fallbackToDestructiveMigration`, senão apaga dados de usuários existentes).
* **Relations:** `FullWorkout`, `WorkoutExerciseWithSets`.
* **Domain models:** `Workout.id`, `WorkoutExercise.id`, `ExerciseSet.id` (e os campos de FK correspondentes).
* **Novo campo:** `updatedAt: Long` em `WorkoutEntity`/`UserEntity`, necessário pro LWW da Fase 6.
* **Navigation:** `NavRoutes` usa `NavType.LongType`/`IntType` para `workoutId` em várias rotas — precisa virar `StringType`.
* **\~30-40 arquivos afetados**, incluindo: `WorkoutRepositoryImpl`, `WorkoutDao`/`UserDao`, todos os UseCases que recebem `workoutId: Long` (`GetWorkoutByIdUseCase`, `SaveWorkoutUseCase`, `UpdateWorkoutUseCase`, `DeleteWorkoutUseCase`, `GetWorkoutDoneByIdUseCase`...), `WorkoutUIModel.id: Int`, `WorkoutResumeViewModel(workoutId: Long?, ...)`, `ConfigureWorkoutViewModel`, `WorkoutStartViewModel`, `WorkoutPreviewViewModel`, e as Screens que fazem `.toInt()`/`.toLong()` em IDs de treino.

### Ordem sugerida para a Fase 4 (fazer numa sessão dedicada, com build local disponível):

1. Trocar tipos nas Entities + escrever a `Migration` do Room primeiro, isoladamente, e compilar só o módulo de dados.
2. Atualizar `Workout`/`WorkoutExercise`/`ExerciseSet` (domain) e todos os UseCases que os tocam.
3. Atualizar `NavRoutes`/`NavGraph` para `StringType`.
4. Atualizar `WorkoutUIModel` e todas as Screens/ViewModels que fazem `.toInt()`/`.toLong()` em id de treino (esses casts todos devem desaparecer).
5. Rodar `./gradlew build` a cada etapa — essa fase não deveria ser feita "a olho" como as anteriores.

## 🔜 Fase 5 — LocalDataSource (não iniciada)

`OnboardingRepositoryImpl`/`WorkoutRepositoryImpl` ainda acessam `UserDao` direto. Criar `OnboardingLocalDataSource`/`WorkoutLocalDataSource`.

## 🔜 Fase 6 — Dual-write Room + Firestore (não iniciada)

Depende da Fase 4 (UUID) e Fase 5 (LocalDataSource) prontas.

## 🔜 Fase 7 — Sessão via FirebaseAuth (não iniciada)

`AuthRepository.isLoggedIn()` sobre `firebaseAuth.currentUser`; `SplashViewModel` decide rota por isso.

## 🔜 Fase 8 — Cleanup de Compose (não iniciada)

`key` faltando em `LazyColumn`/`itemsIndexed` em: `MuscleGroupSelectionScreen` (já corrigido na Fase 1, conferir se sobrou algum), `WorkoutScreen` (histórico), `NewWorkoutScreen` (já corrigido), `WorkoutStartScreen`, `WorkoutPreviewScreen`, `CreateWorkoutComponents` (sets). `@Immutable`/`ImmutableList` nos `UiState` com listas.

## Backlog (fora de escopo por decisão sua)

* Testes automatizados
* Modularização por feature
* Write-through com fila de retry pro Firestore (se sync-só-no-login se mostrar insuficiente)

