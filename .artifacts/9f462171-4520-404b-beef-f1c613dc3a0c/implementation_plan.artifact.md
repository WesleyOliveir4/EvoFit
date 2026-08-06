# Plano de Implementação - Ícone de Informação e Overlay na Tela de Execução de Treino

Este plano descreve as alterações para adicionar um ícone de informação "i" (badge verde) ao lado do número do exercício na tela `WorkoutStartScreen`, permitindo visualizar a imagem do exercício em um overlay, similar à funcionalidade já implementada na seleção de exercícios.

## User Review Required

> [!IMPORTANT]
> O ícone será implementado como um pequeno badge verde com um "i" preto, posicionado de forma a sobrepor levemente o círculo do índice do exercício, conforme a imagem de referência fornecida.

## Proposed Changes

### [UI Components]

#### [MODIFY] [StartWorkoutComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/startworkout/components/StartWorkoutComponents.kt)
- Atualizar `ExerciseTrackingCard`:
    - Adicionar o parâmetro `onInfoClick: (String) -> Unit`.
    - Modificar o layout do índice: Envolver o círculo do número em um `Box`.
    - Adicionar o badge "i": um círculo verde (`MaterialTheme.colorScheme.primary`) com um "i" preto, posicionado no canto inferior direito do círculo do índice.
    - Tornar o badge clicável para disparar o `onInfoClick(exercise.exerciseId)`.

### [Screens]

#### [MODIFY] [WorkoutStartScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/startworkout/screens/WorkoutStartScreen.kt)
- Adicionar o estado `showImageForExerciseId: String?` para controlar a exibição da imagem.
- Implementar o `Dialog` de overlay de imagem no final da `WorkoutStartScreen` (replicando a lógica da `SelectExercisesScreen`).
- Atualizar o `BackHandler` para fechar o overlay se estiver aberto.
- Atualizar `WorkoutStartContent` e a chamada no `LazyColumn` para propagar o callback `onInfoClick` até o `ExerciseTrackingCard`.

## Verification Plan

### Manual Verification
- [ ] Iniciar um treino.
- [ ] Verificar se o badge verde com "i" aparece ao lado do número de cada exercício.
- [ ] Clicar no badge "i" e validar se o overlay com a imagem do exercício abre corretamente.
- [ ] Validar se clicar fora da imagem ou no botão voltar fecha o overlay.
- [ ] Confirmar se o mapeamento de imagens (ex: Elevação Frontal) funciona corretamente nesta tela.
