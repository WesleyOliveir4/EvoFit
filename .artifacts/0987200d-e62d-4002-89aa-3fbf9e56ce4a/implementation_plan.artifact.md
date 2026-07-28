# Plano de Implementação - Otimização e Migração do Histórico de Treinos

Este plano visa otimizar a consulta do histórico de treinos (limitando aos últimos 7) e migrar a estrutura de armazenamento de "documento único" para "documentos individuais". Isso resolve o problema de performance e o limite de 1MB do Firestore.

## User Review Required

> [!IMPORTANT]
> **Estratégias de Migração:** A migração será feita de forma transparente no primeiro acesso ao histórico após a atualização. Os dados antigos serão lidos do formato antigo, salvos no novo formato individual e o registro antigo será removido.
>
> **Impacto nas Estatísticas:** A tela principal mostra "Treinos nesta semana". Se limitarmos a busca a apenas 7 itens no repositório, o cálculo de treinos da semana no ViewModel pode ficar incorreto se o usuário treinar mais de 7 vezes em 7 dias.
> *   **Proposta:** Manteremos uma consulta separada ou um parâmetro opcional no repositório para garantir que estatísticas não sejam quebradas.

## Mudanças Propostas

### 1. Modelo de Domínio
#### [MODIFY] [WorkoutDone.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/domain/model/WorkoutDone.kt)
*   Adicionar campo `createdAt: Long = System.currentTimeMillis()` para ordenação eficiente no banco de dados.

### 2. Armazenamento Local (Room)
#### [NEW] `WorkoutDoneEntity.kt`
*   Criar entidade para representar um único treino finalizado.
*   Campos: `id` (PK), `userId`, `name`, `muscleGroupId`, `date`, `exercises` (JSON via converter), `time`, `createdAt`.

#### [MODIFY] [UserDao.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/local/dao/UserDao.kt)
*   Adicionar `@Insert` para `WorkoutDoneEntity`.
*   Adicionar `@Query("SELECT * FROM workout_done WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit")`.
*   Manter temporariamente métodos da tabela antiga para migração.

#### [MODIFY] [AppDatabase.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/local/AppDatabase.kt)
*   Incluir `WorkoutDoneEntity` nas entities.
*   Criar `MIGRATION_8_9` para criar a nova tabela.

### 3. Armazenamento Remoto (Firestore)
#### [MODIFY] [WorkoutRemoteDataSource.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/datasource/WorkoutRemoteDataSource.kt)
*   `saveWorkoutDoneHistory`: Alterar para salvar em `/users/{userId}/history/{workoutId}`.
*   Adicionar `getLatestWorkoutDoneHistory(userId, limit)` usando `orderBy("createdAt", DESC)`.
*   Adicionar `deleteOldHistorySummary(userId)` para limpar o documento antigo após migração.

### 4. Repositório e Migração
#### [MODIFY] [WorkoutRepositoryImpl.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/repository/WorkoutRepositoryImpl.kt)
*   Implementar lógica de migração:
    1.  Ao buscar histórico, verificar se a nova tabela está vazia.
    2.  Se sim, buscar na tabela/documento antigo.
    3.  Se houver dados antigos, mapear para o novo formato, salvar individualmente e deletar o antigo.

### 5. Apresentação
#### [MODIFY] [WorkoutViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/home/viewmodel/WorkoutViewModel.kt)
*   Atualizar a chamada do UseCase para passar o limite de 7 treinos para exibição na Home.

## Plano de Verificação

### Testes Automatizados
*   Testar o `UserDao` para garantir que `LIMIT 7` funciona e a ordenação por `createdAt` está correta.
*   Testar a lógica de migração no `WorkoutRepositoryImpl` simulando dados no formato antigo.

### Verificação Manual
1.  Abrir o app com dados antigos e verificar se os treinos aparecem normalmente (Migração disparada).
2.  Verificar no Logcat os logs de migração.
3.  Verificar no Firebase Console se a estrutura mudou de um documento `summary` para vários documentos na subcoleção `history`.
