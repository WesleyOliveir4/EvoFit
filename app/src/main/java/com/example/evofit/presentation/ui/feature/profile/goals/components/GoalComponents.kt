package com.example.evofit.presentation.ui.feature.profile.goals.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.AppGreen
import com.example.evofit.presentation.ui.theme.AppSurface
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextPrimary
import com.example.evofit.presentation.ui.theme.TextSecondary

@Composable
fun GoalCard(
    title: String,
    category: String,
    currentValue: String,
    targetValue: String,
    percentage: Int,
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val evoPurpleTagBg = Color(0xFF261A35)
    val evoPurpleTagText = Color(0xFFA855F7)
    val deleteContentDescription = stringResource(id = R.string.profile_goal_delete_description)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Linha Superior: Título, Porcentagem e Deletar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$percentage%",
                        color = AppGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = deleteContentDescription,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Tag da Categoria (Ex: Peso, Exercício, Cardio)
            Box(
                modifier = Modifier
                    .background(evoPurpleTagBg, RoundedCornerShape(8.dp))
                    .padding(horizontal = 5.dp, vertical = 4.dp)
            ) {
                Text(
                    text = category,
                    color = evoPurpleTagText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Barra de Progresso Customizada
            val progressFraction = (percentage / 100f).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = AppGreen,
                trackColor = Color(0xFF2C2C2E),
                strokeCap = StrokeCap.Round
            )

            // Linha Inferior: Atual vs Meta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = stringResource(id = R.string.profile_goals_label_current), color = TextSecondary, fontSize = 13.sp)
                    Text(text = currentValue, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = stringResource(id = R.string.profile_goals_label_target), color = TextSecondary, fontSize = 13.sp)
                    Text(text = targetValue, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(name = "Força", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StrengthGoalPreview() {
    EvoFitTheme {
        Surface(color = AppDarkBg) {
            GoalCard(
                title = "Supino reto 100kg",
                category = "Força",
                currentValue = "90kg",
                targetValue = "100kg",
                percentage = 90,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "Cardio", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CardioGoalPreview() {
    EvoFitTheme {
        Surface(color = AppDarkBg) {
            GoalCard(
                title = "Corrida 5km",
                category = "Cardio",
                currentValue = "2.5km",
                targetValue = "5.0km",
                percentage = 50,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "Peso", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WeightGoalPreview() {
    EvoFitTheme {
        Surface(color = AppDarkBg) {
            GoalCard(
                title = "Peso Corporal",
                category = "Peso",
                currentValue = "82kg",
                targetValue = "75kg",
                percentage = 30,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
