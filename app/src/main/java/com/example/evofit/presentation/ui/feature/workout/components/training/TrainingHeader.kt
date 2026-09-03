package com.example.evofit.presentation.ui.feature.workout.components.training

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evofit.R
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun HeaderSection(
    userName: String,
    modifier: Modifier = Modifier,
    isSyncing: Boolean = false,
    isOnline: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "syncRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.main_workout_greeting, userName),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = stringResource(R.string.main_workout_subtitle),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Box(
            modifier = Modifier.size(Dimens.MinimumTouchTarget + 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.MinimumTouchTarget)
                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_logo_evofit),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimens.IconSizeLarge)
                )
            }

            if (isSyncing || !isOnline) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(Dimens.IconSizeSmall)
                        .background(
                            if (isSyncing) MaterialTheme.colorScheme.primary 
                            else MaterialTheme.colorScheme.error, 
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isSyncing) Icons.Default.Sync else Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = if (isSyncing) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onError,
                        modifier = Modifier
                            .size(Dimens.SpacingMediumSmall)
                            .then(if (isSyncing) Modifier.rotate(rotation) else Modifier)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun HeaderSectionPreview() {
    EvoFitTheme {
        Column(
            modifier = Modifier.padding(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
        ) {
            HeaderSection(userName = "Gabriel", isOnline = true, isSyncing = false)
            HeaderSection(userName = "Gabriel", isOnline = true, isSyncing = true)
            HeaderSection(userName = "Gabriel", isOnline = false, isSyncing = false)
        }
    }
}
