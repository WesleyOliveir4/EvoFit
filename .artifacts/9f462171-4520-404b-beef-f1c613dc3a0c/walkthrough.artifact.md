# Walkthrough - Refatoração da Seleção de Exercícios

As alterações solicitadas foram implementadas com sucesso. O seletor de exercícios foi movido para a direita, e agora é possível visualizar a imagem de execução de cada exercício através de um ícone de informação.

## Alterações Realizadas

### Componentes de UI
- **[ExerciseRowItem](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/components/configure/ExerciseSelectionComponents.kt):**
    - O seletor (Check) foi movido para a extremidade direita da linha.
    - Foi adicionado um `IconButton` com o ícone `Info` ("i") à esquerda do nome do exercício.
    - O layout foi ajustado para garantir o alinhamento correto e espaçamento adequado.

### Mapeamento de Imagens
- **[ExerciseMapper](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/mapper/ExerciseMapper.kt):**
    - Criado um novo objeto para gerenciar o vínculo entre o ID do exercício e o recurso de imagem (`drawable`).
    - Regra aplicada: ID "46" mapeia para `img_elevacao_frontal`, todos os outros mapeiam para `img_cardio`.

### Overlay de Imagem
- **[SelectExercisesScreen](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/createworkout/screens/SelectExercisesScreen.kt):**
    - Implementado um estado local para controlar qual imagem está sendo exibida.
    - Utilizado um `Dialog` com fundo ofuscado (70% de opacidade preta) para exibir a imagem.
    - A imagem é exibida centralizada horizontalmente e com um deslocamento para cima (`offset(y = -50.dp)`), conforme solicitado.
    - O fechamento do overlay ocorre ao clicar fora da imagem ou ao pressionar o botão voltar do sistema.

## Verificação

> [!TIP]
> Você pode testar clicando no ícone "i" de qualquer exercício. O exercício "Elevação Frontal" (ID 46) mostrará a imagem específica, enquanto os demais mostrarão a imagem padrão de cardio.

### Testes Manuais Sugeridos
1. **Seleção:** Verifique se clicar na linha ainda marca/desmarca o exercício sem abrir a imagem.
2. **Overlay:** Clique no "i" e verifique se o fundo fica escurecido.
3. **Fechamento:** Toque na área escura ao redor da imagem ou use o botão voltar para fechar.
4. **Alinhamento:** Confirme se o seletor circular agora aparece à direita.

---

# Ajustes na Execução de Treino (WorkoutStartScreen)

Implementamos o ícone de informação também na tela de início de treino, permitindo visualizar as imagens de execução durante a prática do exercício.

## Alterações Realizadas

### Componentes de Execução
- **[StartWorkoutComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/startworkout/components/StartWorkoutComponents.kt):**
    - Adicionado um badge verde com "i" preto ao lado do número do exercício no `ExerciseTrackingCard`.

### Tela de Treino
- **[WorkoutStartScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/startworkout/screens/WorkoutStartScreen.kt):**
    - Integrado o overlay de imagem com suporte ao botão voltar do sistema.
