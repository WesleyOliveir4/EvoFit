# Remover toIcon e Unificar Uso de Imagens

Como agora existem imagens para todos os grupos musculares, a função `.toIcon()` e a propriedade `icon` no `MuscleGroupItem` tornaram-se redundantes. O objetivo é simplificar o código utilizando apenas `imageRes` (imagens `.webp`).

## Alterações Propostas

### [Modelo de Dados]

#### [MODIFY] [MuscleGroupItem.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/model/MuscleGroupItem.kt)
- Remover a propriedade `icon: ImageVector?`.

### [Mapeamento]

#### [MODIFY] [MuscleGroupMapper.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/mapper/MuscleGroupMapper.kt)
- Remover a função de extensão `MuscleGroupType.toIcon()`.
- Atualizar `MuscleGroup.toItem()` para não tentar mapear o ícone.

### [UI Components]

#### [MODIFY] [ExerciseSelectionComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/components/configure/ExerciseSelectionComponents.kt)
- Atualizar `MuscleGroupCard` para remover a lógica de fallback para o ícone.
- Atualizar o preview do componente.

#### [MODIFY] [CreateWorkoutComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/createworkout/components/CreateWorkoutComponents.kt)
- Atualizar `ExerciseConfigHeader` para usar `imageRes` via `toImageRes()` em vez de `toIcon()`.
- Substituir o `Icon` por um `Image` com `ContentScale.Crop`.

#### [MODIFY] [NewWorkoutScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/workout/createworkout/screens/NewWorkoutScreen.kt)
- Atualizar o preview para remover a passagem do parâmetro `icon`.

## Plano de Verificação

### Verificação Manual
- Validar se todas as telas (Seleção de Grupos Musculares e Configuração de Exercícios) estão exibindo as imagens corretamente.
- Garantir que o círculo de ícone no cabeçalho da configuração agora exibe a imagem do grupo muscular recortada.
