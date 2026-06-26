package com.example.evofit.presentation.ui.feature.onboard.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.presentation.ui.feature.onboard.viewmodel.OnboardingViewModel
import org.koin.androidx.compose.koinViewModel

private val AppDarkBg = Color(0xFF090909)
private val AppSurface = Color(0xFF1E1E1E)
private val AppGreen = Color(0xFF5ED961)
private val IconContainerBg = Color(0xFF1A231A)
private val TextPrimary = Color(0xFFFFFFFF)
private val TextSecondary = Color(0xFF8E8E93)

@Composable
fun OnboardSummaryScreen(
    onStartTraining: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val userData by viewModel.userData.collectAsState()

    OnboardSummaryContent(
        userData = userData,
        onStartTraining = { 
            viewModel.finishOnboarding(onStartTraining) 
        }
    )
}

@Composable
fun OnboardSummaryContent(
    userData: UserOnboardingData,
    onStartTraining: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppDarkBg)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Tudo pronto!",
                color = TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Seu perfil está configurado.",
                color = TextSecondary,
                fontSize = 16.sp
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AppSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "RESUMO",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                SummaryRow(
                    icon = Icons.Default.AccountCircle,
                    label = "Nome",
                    value = userData.name
                )

                SummaryRow(
                    icon = Icons.Default.DateRange,
                    label = "Idade",
                    value = "${userData.age} anos"
                )

                SummaryRow(
                    icon = Icons.Default.Favorite,
                    label = "Peso",
                    value = "${userData.weight} kg"
                )

                if (userData.goals.isNotEmpty()) {
                    SummaryRow(
                        icon = Icons.Default.Star,
                        label = "Metas Definidas",
                        value = "${userData.goals.size}"
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Button(
                onClick = onStartTraining,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AppGreen)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "🔥", fontSize = 18.sp) 
                    
                    Text(
                        text = "Começar a Treinar",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(8.dp).background(Color(0xFF2C2C2E), CircleShape))
                Box(modifier = Modifier.size(8.dp).background(Color(0xFF2C2C2E), CircleShape))
                Box(
                    modifier = Modifier
                        .size(width = 24.dp, height = 8.dp)
                        .background(AppGreen, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
fun SummaryRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(IconContainerBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AppGreen,
                modifier = Modifier.size(24.dp)
            )
        }

        Column {
            Text(
                text = label,
                color = TextSecondary,
                fontSize = 14.sp
            )
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview
@Composable
fun OnboardSummaryScreenPreview() {
    OnboardSummaryContent(
        userData = UserOnboardingData(
            name = "Wesley",
            age = "28",
            weight = "78",
            height = "175",
            goals = emptyList()
        ),
        onStartTraining = {}
    )
}
