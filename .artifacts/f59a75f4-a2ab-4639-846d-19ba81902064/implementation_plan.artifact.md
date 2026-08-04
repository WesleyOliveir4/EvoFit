# Unificar Imagens de Grupos Musculares na WorkoutScreen

O objetivo é garantir que todos os componentes da `WorkoutScreen` que representam um treino (Treinos Atuais, Sessão Ativa e Histórico) utilizem a imagem do grupo muscular correspondente. Seguindo a orientação do usuário, para treinos com múltiplos grupos, será utilizada a imagem do primeiro grupo associado.

## Mudanças Propostas

### [Modelos de UI]

#### [MODIFY] [WorkoutHomeUIModels.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/model/WorkoutHomeUIModels.kt)
- Adicionar `val imageRes: Int? = null` aos modelos `WorkoutHistoryUIModel` e `ActiveSessionUIModel`.

### [Mapeamento de Dados]

#### [MODIFY] [WorkoutViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/home/viewmodel/WorkoutViewModel.kt)
- Atualizar o mapeamento do histórico (`history`) para preencher `imageRes` usando `workoutDone.muscleGroup?.type?.toImageRes()`.
- Atualizar o mapeamento da sessão ativa (`activeSession`) para preencher `imageRes` usando `activeSession.workout.muscleGroup?.type?.toImageRes()`.

### [Componentes de UI]

#### [MODIFY] [ActiveWorkoutCard.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/components/training/ActiveWorkoutCard.kt)
- Adicionar parâmetro `imageRes: Int?` ao componente.
- Substituir o ícone fixo por um componente `Image` que renderiza o `imageRes` (com fallback para o ícone de peso se nulo).

#### [MODIFY] [WorkoutDoneItem.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/components/training/WorkoutDoneItem.kt)
- Adicionar um Box de ícone/imagem similar ao do `WorkoutListItem`.
- Renderizar a imagem do grupo muscular do histórico.

#### [MODIFY] [WorkoutScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/home/screens/WorkoutScreen.kt)
- Passar o `imageRes` para o `ActiveWorkoutCard`.

## Plano de Verificação

### Verificação Manual
- Validar no app/preview se o card de "Sessão Ativa" exibe a imagem correta.
- Validar se os itens do "Histórico" também exibem as imagens dos grupos musculares.
- Confirmar que a imagem utilizada é sempre a do primeiro grupo (conforme definido no salvamento do treino).
