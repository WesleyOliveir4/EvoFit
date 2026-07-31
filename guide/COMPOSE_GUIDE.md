# Guia de Estilo e Boas Práticas - Jetpack Compose (EvoFit)

Este documento serve como referência para o desenvolvimento de UI utilizando Jetpack Compose no projeto EvoFit, garantindo consistência, performance e manutenibilidade.

---

## 1. Princípios de Estrutura e Layout

### 1.1 Scaffold como Base
Toda tela principal deve ser estruturada utilizando o `Scaffold`.
- **Regra:** Sempre utilize o `innerPadding` fornecido pelo `Scaffold` no conteúdo principal para evitar sobreposição com barras do sistema.
- **Componentes:** Centralize o uso de `TopBar`, `BottomBar`, `FloatingActionButton` (FAB) e `SnackbarHost` através das propriedades do `Scaffold`.

### 1.2 Listas Eficientes
Prefira `LazyColumn` ou `LazyRow` em vez de `Column` com `verticalScroll`.
- **Performance:** Utilize o parâmetro `key` em listas para otimizar recomposições.
- **Scroll:** Apenas o conteúdo da lista deve rolar; Headers, BottomBars e FABs devem permanecer fixos.

### 1.3 Dimensionamento Responsivo
- **Evite Alturas Fixas:** Prefira `wrapContentHeight()`, `fillMaxWidth()` e o uso de `weight()`.
- **Uso de Weight:** Utilize `Modifier.weight()` para fazer com que listas ou componentes ocupem apenas o espaço disponível restante.
- **Espaçamento:** Evite `Spacer` com valores fixos gigantes. Prefira `Arrangement.spacedBy()` em Columns e Rows.

---

## 2. Design System e Reutilização

### 2.1 Componentes Base
Utilize e mantenha os componentes padronizados do projeto:
- `EvoButton`: Botão primário e secundário.
- `EvoTextField`: Campos de entrada de texto.
- `WorkoutCard`: Card para exibição de treinos.
- `StatCard`: Card para exibição de estatísticas.

### 2.2 Centralização de Dimensões
Utilize o objeto `Dimens` para manter espaçamentos consistentes (a ser implementado/padronizado):

```kotlin
object Dimens {
    val XS = 4.dp
    val S = 8.dp
    val M = 16.dp
    val L = 24.dp
    val XL = 32.dp
}
```

### 2.3 Tema e Recursos
- **MaterialTheme:** Sempre utilize `MaterialTheme.colorScheme`, `MaterialTheme.typography` e `MaterialTheme.shapes`.
- **Strings:** Nunca utilize textos fixos (hardcoded). Use sempre `stringResource(R.string.exemplo)`.
- **Window Insets:** Garanta suporte a Edge-to-Edge utilizando `enableEdgeToEdge()` e tratando insets com `WindowInsets.safeDrawing`.

---

## 3. Arquitetura da UI e Estado

### 3.1 Desacoplamento
- **Stateless Composables:** Componentes devem ser desacoplados. Eles recebem o **estado** e expõem **callbacks** para eventos.
- **ViewModel:** Não passe `ViewModel` diretamente para componentes pequenos/reutilizáveis. O ViewModel deve ser acessado apenas no nível da tela (Screen level).

### 3.2 Gestão de Estado
- **UI State:** Utilize uma `data class` única por tela para representar o estado da UI.
- **Imutabilidade:** Mantenha os estados imutáveis, utilizando `.copy()` para atualizações.
- **Remember:** Utilize `remember` e `rememberSaveable` para evitar a recriação de objetos pesados durante recomposições.
- **Lógica:** Zero lógica na UI. Cálculos, formatações complexas e regras de negócio devem residir no `ViewModel` ou na camada de `Domain`.

---

## 4. Testes e Adaptabilidade

### 4.1 Visualização (Preview)
Sempre crie Previews para seus componentes cobrindo:
- Light Mode e Dark Mode.
- Diferentes tamanhos de fonte.
- Telas pequenas e grandes.

### 4.2 Suporte a Múltiplos Dispositivos
Utilize `WindowSizeClass` para adaptar o layout conforme o tamanho da tela:
- **Compact:** Phones.
- **Medium:** Foldables / Small Tablets.
- **Expanded:** Tablets / Desktop.

### 4.3 Acessibilidade
- **Área de Toque:** Garanta uma área mínima de toque de `48.dp`.
- **Descrições:** Utilize `contentDescription` em elementos visuais que não possuem texto.

---

## 5. Tabela de Referência Material 3 (EvoFit)

| Componente | Valor Recomendado |
| :--- | :--- |
| **Área mínima de toque** | 48.dp |
| **Botão Primário** | 56.dp (altura) |
| **Botão Secundário** | 48.dp (altura) |
| **TextField / Search Bar** | 56.dp (altura) |
| **FAB (Padrão)** | 56.dp |
| **Small FAB** | 40.dp |
| **Large FAB** | 96.dp |
| **Ícone (Padrão)** | 24.dp |
| **Ícone (Grande)** | 32.dp |
| **Avatar** | 56.dp |
| **Bottom Navigation** | 80.dp |
| **Top App Bar (Small)** | 64.dp |
| **Top App Bar (Medium)** | 112.dp |
| **Top App Bar (Large)** | 152.dp |
| **Espaçamento Pequeno** | 8.dp |
| **Espaçamento Padrão** | 16.dp |
| **Espaçamento Grande** | 24.dp |
| **Espaçamento entre Seções** | 32.dp |
| **Padding Horizontal/Vertical** | 16.dp |
| **Raio Padrão (Shapes)** | 16.dp |
| **Raio de Cards** | 20.dp - 24.dp |

---

## Resumo para o Desenvolvedor EvoFit
1. Use **Scaffold** sempre.
2. Use **LazyColumn** como contêiner principal de conteúdo.
3. Garanta que **apenas a lista faça scroll**.
4. Siga as alturas padrão (**56.dp** para botões e campos).
5. Mantenha os **Cards consistentes** com o Design System.
