package com.guaracode.evofit.presentation.ui.feature.workout.components.training

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun OfflineToast(
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.95f),
        shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
        shadowElevation = Dimens.SpacingExtraSmall,
        modifier = modifier
            .padding(Dimens.SpacingMedium)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingMediumSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMediumSmall)
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(Dimens.IconSizeDefault)
            )
            Text(
                text = "Modo offline. Alterações serão sincronizadas em breve.",
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview
@Composable
private fun OfflineToastPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            OfflineToast()
        }
    }
}
