# Implementação de Histórico de Peso (Weight History)

Este plano descreve as alterações necessárias para adicionar o rastreamento de histórico de peso no aplicativo EvoFit. O sistema registrará cada atualização de peso tanto localmente (Room) quanto remotamente (Firestore).

## User Review Required

> [!IMPORTANT]
> A atualização da versão do banco de dados Room exigirá uma migração. Como o projeto já possui várias migrações, adicionarei a `MIGRATION_12_13`.
> O formato de data será `dd/MM/yyyy` conforme solicitado (PT-BR).

## Proposed Changes

### Domain Layer

#### [NEW] [WeightUpdate](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/domain/model/WeightUpdate.kt)
Define o modelo de domínio para uma atualização de peso.

#### [MODIFY] [OnboardingRepository](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/domain/repository/OnboardingRepository.kt)
Adicionar métodos para salvar e obter o histórico de peso.

#### [NEW] [AddWeightUpdateUseCase](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/domain/usecase/AddWeightUpdateUseCase.kt)
Caso de uso para registrar um novo peso no histórico.

---

### Data Layer

#### [NEW] [WeightUpdateEntity](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/local/entities/WeightUpdateEntity.kt)
Entidade Room para persistência local do histórico.

#### [NEW] [WeightHistoryDao](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/local/dao/WeightHistoryDao.kt)
Interface DAO para operações no histórico de peso.

#### [MODIFY] [AppDatabase](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/local/AppDatabase.kt)
Incluir a nova entidade e incrementar a versão do banco.

#### [MODIFY] [UserLocalDataSource](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/datasource/UserLocalDataSource.kt)
Adicionar operações de histórico de peso.

#### [MODIFY] [UserRemoteDataSource](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/datasource/UserRemoteDataSource.kt)
Adicionar suporte para salvar no Firestore (sub-coleção `weight_history`).

#### [MODIFY] [OnboardingRepositoryImpl](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/data/repository/OnboardingRepositoryImpl.kt)
Implementar a lógica de salvar e recuperar do histórico, integrando local e remoto.

---

### Presentation Layer

#### [MODIFY] [OnboardingViewModel](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/onboard/viewmodel/OnboardingViewModel.kt)
Chamar o registro de peso ao finalizar o onboarding.

#### [MODIFY] [UserDataViewModel](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/profile/userdata/viewmodel/UserDataViewModel.kt)
Chamar o registro de peso se houver alteração no valor durante a edição do perfil.

## Verification Plan

### Automated Tests
- Criar teste unitário para `AddWeightUpdateUseCase`.
- Verificar se a data está sendo formatada corretamente em PT-BR.

### Manual Verification
1. Completar o Onboarding e verificar no Logcat/Firestore se o primeiro peso foi registrado.
2. Alterar o peso no perfil e verificar se uma nova entrada surgiu no histórico.
3. Deslogar e verificar se a tabela local foi limpa.
4. Logar novamente e verificar se os dados são sincronizados (opcional, dependendo da implementação do sync).
