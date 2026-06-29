package com.example.evofit.presentation.ui.feature.workout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.EvoFitTheme

data class WorkoutUIModel(
    val id: Int,
    val title: String,
    val exercises: Int,
    val series: Int,
    val icon: ImageVector
)

@Composable
fun HeaderSection(
    userName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = stringResource(R.string.main_workout_greeting, userName),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.main_workout_subtitle),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp
            )
        }

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun WorkoutListItem(
    workout: WorkoutUIModel,
    modifier: Modifier = Modifier,
    handleModifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = workout.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workout.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                val exercisesStr = stringResource(R.string.main_workout_exercise_count, workout.exercises)
                val seriesStr = stringResource(R.string.main_workout_series_count, workout.series)
                Text(
                    text = stringResource(R.string.main_workout_exercise_series_format, exercisesStr, seriesStr),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp
                )
            }

            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = stringResource(R.string.main_workout_drag_handle_desc),
                tint = MaterialTheme.colorScheme.outline,
                modifier = handleModifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun HeaderSectionPreview() {
    EvoFitTheme {
        HeaderSection(userName = "Wesley", modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun StatCardPreview() {
    EvoFitTheme {
        StatCard(
            value = "5",
            label = "Treinos",
            icon = Icons.Default.Favorite,
            modifier = Modifier.padding(16.dp).width(160.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun WorkoutListItemPreview() {
    EvoFitTheme {
        WorkoutListItem(
            workout = WorkoutUIModel(
                id = 1,
                title = "Peito",
                exercises = 3,
                series = 7,
                icon = Icons.Default.Favorite
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
