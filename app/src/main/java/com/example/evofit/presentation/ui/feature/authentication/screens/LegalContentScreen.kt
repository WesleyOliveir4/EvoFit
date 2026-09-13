package com.example.evofit.presentation.ui.feature.authentication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.presentation.ui.feature.components.TopBarReturn
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun LegalContentScreen(
    type: String,
    onBackClick: () -> Unit
) {
    val isTerms = type == "terms"
    val title = if (isTerms) "Termos de Uso" else "Política de Privacidade"
    val lastUpdated = "Última atualização: 25 de maio de 2025" // As per user request/example

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            TopBarReturn(
                onBackClick = onBackClick,
                title = title,
                subtitle = lastUpdated
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            if (isTerms) {
                TermsOfUseContent()
            } else {
                PrivacyPolicyContent()
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingExtraExtraLarge))
        }
    }
}

@Composable
private fun TermsOfUseContent() {
    LegalSection(
        number = "1",
        title = "Sobre o EvoFit",
        content = "O EvoFit é um aplicativo desenvolvido para auxiliar no acompanhamento e na evolução dos seus treinos. O aplicativo permite registrar exercícios, cargas, séries, repetições, metas e outros dados relacionados ao seu desempenho, além de visualizar sua evolução por meio de gráficos e históricos."
    )
    LegalSection(
        number = "2",
        title = "Uso do aplicativo",
        content = "Ao utilizar o EvoFit, você concorda em utilizar o aplicativo de forma adequada e de acordo com a legislação aplicável.\n\nO EvoFit é uma ferramenta de acompanhamento e organização dos seus treinos e não substitui a orientação de profissionais de educação física, médicos ou outros profissionais de saúde."
    )
    LegalSection(
        number = "3",
        title = "Seus registros",
        content = "Os dados inseridos por você, como treinos, cargas, séries, repetições e metas, são utilizados para fornecer as funcionalidades de acompanhamento e evolução disponíveis no aplicativo."
    )
    LegalSection(
        number = "4",
        title = "Conta do usuário",
        content = "Algumas funcionalidades do EvoFit dependem da criação de uma conta. Você é responsável por manter a segurança das suas credenciais de acesso e por utilizar sua conta de forma adequada."
    )
    LegalSection(
        number = "5",
        title = "Disponibilidade do serviço",
        content = "Buscamos manter o EvoFit disponível e funcionando corretamente, mas algumas funcionalidades podem ficar temporariamente indisponíveis devido a manutenção, atualizações, falhas técnicas ou problemas relacionados a serviços de terceiros."
    )
    LegalSection(
        number = "6",
        title = "Alterações",
        content = "Os Termos de Uso podem ser atualizados para refletir mudanças no aplicativo, nos serviços oferecidos ou nas obrigações legais aplicáveis."
    )
    LegalSection(
        number = "7",
        title = "Aceitação",
        content = "Ao criar uma conta e utilizar o EvoFit, você declara que leu e concorda com estes Termos de Uso."
    )
}

@Composable
private fun PrivacyPolicyContent() {
    LegalSection(
        number = "1",
        title = "Quais dados coletamos",
        content = "Para fornecer as funcionalidades do EvoFit, podemos armazenar informações fornecidas por você, incluindo:\n\n• Nome\n• E-mail\n• Data de nascimento\n• Altura\n• Peso\n• Histórico de treinos\n• Metas e informações relacionadas à sua evolução"
    )
    LegalSection(
        number = "2",
        title = "Como utilizamos seus dados",
        content = "Utilizamos essas informações para:\n\n• Personalizar sua experiência no EvoFit;\n• Identificar sua conta;\n• Salvar e recuperar seus treinos;\n• Registrar sua evolução;\n• Calcular e acompanhar suas metas;\n• Exibir gráficos e históricos de desempenho;\n• Permitir que você continue utilizando seus dados em diferentes sessões do aplicativo."
    )
    LegalSection(
        number = "3",
        title = "Armazenamento dos dados",
        content = "Algumas informações do EvoFit são armazenadas localmente no dispositivo utilizando o banco de dados Room.\n\nDeterminadas informações também podem ser enviadas e armazenadas em servidores para permitir a sincronização e a manutenção dos dados associados à sua conta."
    )
    LegalSection(
        number = "4",
        title = "Firebase",
        content = "O EvoFit utiliza serviços do Firebase para determinadas funcionalidades relacionadas à conta e ao armazenamento de dados.\n\nOs dados enviados aos serviços do Firebase podem incluir informações da sua conta e dados relacionados aos seus treinos, conforme necessário para o funcionamento do aplicativo."
    )
    LegalSection(
        number = "5",
        title = "Segurança",
        content = "Adotamos medidas técnicas para proteger os dados armazenados e limitar o acesso às informações da sua conta.\n\nEntretanto, nenhum sistema de armazenamento ou transmissão de dados pode ser considerado completamente seguro."
    )
    LegalSection(
        number = "6",
        title = "Seus dados",
        content = "Os dados associados à sua conta são utilizados para fornecer as funcionalidades do EvoFit e não devem ser utilizados para finalidades diferentes das descritas nesta Política de Privacidade."
    )
    LegalSection(
        number = "7",
        title = "Exclusão da conta e dos dados",
        content = "Você poderá solicitar a exclusão da sua conta e dos dados associados a ela, observadas as limitações técnicas e legais aplicáveis."
    )
    LegalSection(
        number = "8",
        title = "Alterações nesta Política",
        content = "Esta Política de Privacidade poderá ser atualizada quando houver mudanças no EvoFit, na forma como os dados são tratados ou nas exigências legais aplicáveis."
    )
}

@Composable
private fun LegalSection(
    number: String,
    title: String,
    content: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpacingMediumSmall)
    ) {
        Text(
            text = "$number. $title",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(Dimens.SpacingSmall))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LegalContentScreenPreview() {
    EvoFitTheme {
        LegalContentScreen(type = "privacy", onBackClick = {})
    }
}
