package com.guaracode.evofit.presentation.ui.feature.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.guaracode.evofit.presentation.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarReturn(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    isCenterAligned: Boolean = false,
    showBackIcon: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val titleContent: @Composable () -> Unit = {
        Column(horizontalAlignment = if (isCenterAligned) Alignment.CenterHorizontally else Alignment.Start) {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

    val navigationIcon: @Composable () -> Unit = {
        if (showBackIcon) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.offset(x = Dimens.SpacingNone)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    if (isCenterAligned) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = modifier,
            title = titleContent,
            navigationIcon = navigationIcon,
            actions = actions
        )
    } else {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = modifier,
            title = titleContent,
            navigationIcon = navigationIcon,
            actions = actions
        )
    }
}
