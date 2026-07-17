# EvoFit — Status da Refatoração e Próximos Passos

## ✅ Concluído

### Fase 1 — Padronização de UiModel
* Presentation desacoplada do Domain.
* Novos UiModels: `GoalUIModel`, `WorkoutHistoryUIModel`, `ActiveSessionUIModel`, etc.

### Fase 2 — Corrigir violação Data → Presentation
* `DateMapper` movido para `core/common`.
* Lógica de filtro movida para UseCases.

### Fase 3 — UseCases com Interface + Impl
* Interfaces criadas para todos os UseCases principais.
* Injeção de dependência via Koin atualizada.

### Fase 4 — Migração de IDs para UUID (String)
* **Entidades/Room:** `workoutId`, `exerciseId`, `setId` migrados de `Long` para `String (UUID)`.
* **Migration 4 -> 5:** Implementada para converter dados existentes sem perda.
* **Navigation:** `NavRoutes` atualizado para usar `StringType`.
* **Casts removidos:** `.toInt()` e `.toLong()` eliminados da camada de apresentação.

### Fase 5 — LocalDataSource
* Criados `WorkoutLocalDataSource` e `UserLocalDataSource`.
* Repositórios (`WorkoutRepositoryImpl`, `OnboardingRepositoryImpl`) agora dependem de DataSources em vez de DAOs diretamente.

### Fase 6 — WorkoutSession no Room
* Persistência de `WorkoutSession` movida de `DataStore` (JSON) para tabelas relacionais no Room (`active_session`).
* **Migration 5 -> 6:** Implementada para criar as novas tabelas de sessão ativa.
* Sessão agora sobrevive a reinicializações de forma robusta.

### Fase 7 — Sessão via FirebaseAuth
* `AuthRepository.isLoggedIn()` implementado sobre `firebaseAuth.currentUser`.
* `IsUserLoggedInUseCase` criado e registrado no Koin.
* `SplashViewModel` refatorado para decidir entre Login, Onboarding ou Home.

### Fase 8 — Cleanup de Compose
* `key` em `LazyColumn` e `LazyRow` em todas as telas principais.
* `@Immutable` aplicado em todos os `UiState` da camada de apresentação.
* Refatoração do `WeightWheel` para performance de scroll.

---

### Fase 9 — Firestore Sync (MVP Dual-write)
* Estratégia de "Dual-write" implementada nos repositórios `WorkoutRepositoryImpl` e `OnboardingRepositoryImpl`.
* Sincronização assíncrona para o Firestore via `CoroutineScope(Dispatchers.IO)`.
* Suporte para persistência remota de perfis de usuário, metas, treinos e histórico.
* Uso de UUIDs e `updatedAt` para garantir consistência básica.
