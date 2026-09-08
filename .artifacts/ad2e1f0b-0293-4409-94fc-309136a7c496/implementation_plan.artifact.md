# Plano de Implementação - Estruturação do Fluxo de Metas

Este plano visa reestruturar a tela de metas pessoais (`PersonalGoalsScreen`) e o componente de cartão de meta (`GoalCard`) para seguir o novo design proposto, incluindo ícones por grupo muscular, badges de categoria e filtros.

## Propostas de Mudança

### 1. Data Models & ViewModel

#### [MODIFY] [PersonalGoalsViewModel.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/profile/goals/viewmodel/PersonalGoalsViewModel.kt)
- Atualizar `GoalUiModel` para incluir `iconRes: Int?`.
- Criar um mapa auxiliar no `loadGoals` para associar nomes de exercícios aos seus `MuscleGroupType` correspondentes, permitindo resolver o ícone correto para metas de "Força".
- Atualizar a lógica de mapeamento no `loadGoals`:
    - **Força**: Buscar o ícone baseado no grupo muscular do exercício.
    - **Cardio**: Usar `MuscleGroupType.CARDIO.toImageRes()`.
    - **Peso**: Usar `R.drawable.ic_apple`.

---

### 2. Componentes de UI

#### [MODIFY] [GoalComponents.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/profile/goals/components/GoalComponents.kt)
- Redesenhar o `GoalCard` para um layout horizontal:
    - **Esquerda**: Ícone com background circular/arredondado semi-transparente.
    - **Centro**: Coluna com Título, Badge de Categoria e Informação "Atual • Meta".
    - **Direita**: Porcentagem de progresso e ícone de chevron.
- Adicionar suporte ao `iconRes` no `GoalCard`.
- Adicionar o componente `GoalFilterRow` (chips de filtro: Todas, Força, Cardio, Saúde).

---

### 3. Tela de Metas

#### [MODIFY] [PersonalGoalsScreen.kt](file:///Users/wesleylopesdeoliveira/Documents/ProjetosGit/EvoFit/EvoFit/app/src/main/java/com/example/evofit/presentation/ui/feature/profile/goals/screens/PersonalGoalsScreen.kt)
- Adicionar estado para o filtro selecionado (`Todas`, `Força`, `Cardio`, `Saúde`).
- Integrar o `GoalFilterRow` no topo da lista.
- Filtrar a lista de `goals` exibida com base no chip selecionado.
- Atualizar a chamada do `GoalCard` para passar o novo parâmetro `iconRes`.

## Verificação

### Testes Manuais
- Verificar se cada tipo de meta (Força, Cardio, Peso) exibe o ícone correto.
- Validar se os filtros ("Todas", "Força", etc.) atualizam a lista corretamente.
- Garantir que o layout do `GoalCard` está fiel à imagem de referência.
- Testar a exclusão de metas no novo layout.
