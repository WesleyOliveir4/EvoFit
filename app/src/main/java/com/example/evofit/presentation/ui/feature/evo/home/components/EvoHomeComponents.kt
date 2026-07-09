package com.example.evofit.presentation.ui.feature.evo.home.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun StrengthProgressRow(
    position: String,
    exerciseName: String,
    progressValue: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Posição no ranking (1º, 2º, 3º)
        Text(
            text = position,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(36.dp)
        )
        
        // Nome do Exercício
        Text(
            text = exerciseName,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        
        // Valor da evolução em verde
        Text(
            text = progressValue,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StrengthGainsCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header do Card
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "🏋️", fontSize = 16.sp) // Ícone de Força/Troféu
                Text(
                    text = stringResource(R.string.evo_home_strength_title),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
fun MostEvolvedCard(
    muscleName: String,
    percentage: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "🏅", fontSize = 14.sp) // Medalha/Selo de evolução
                Text(
                    text = stringResource(R.string.evo_home_most_evolved_title),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            
            Column {
                Text(
                    text = muscleName,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = percentage,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun WorkoutsCompletedCard(
    count: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = "🔥", fontSize = 14.sp) // Fogo de sequência de treinos
                Text(
                    text = stringResource(R.string.evo_home_workouts_title),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            
            Column {
                Text(
                    text = count,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 38.sp
                )
                Text(
                    text = stringResource(R.string.evo_home_workouts_completed),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun StrengthGainsCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            StrengthGainsCard {
                StrengthProgressRow(position = "1º", exerciseName = "Supino Reto", progressValue = "+12kg")
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 12.dp))
                StrengthProgressRow(position = "2º", exerciseName = "Agachamento", progressValue = "+10kg")
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun MostEvolvedCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MostEvolvedCard(muscleName = "Peitoral", percentage = "+18%")
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutsCompletedCardPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            WorkoutsCompletedCard(count = "24")
        }
    }
}
