package com.example.evofit.presentation.ui.feature.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.evofit.R
import com.example.evofit.navigation.NavRoutes
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

@Composable
fun AppBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = Dimens.ElevationNone
    ) {
        NavigationBarItem(
            selected = currentRoute == NavRoutes.Home.route,
            onClick = { onNavigate(NavRoutes.Home.route) },
            icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_dumbbell), contentDescription = stringResource(R.string.nav_training)) },
            label = { Text(stringResource(R.string.nav_training)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedTextColor = MaterialTheme.colorScheme.secondary,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == NavRoutes.Evo.route,
            onClick = { onNavigate(NavRoutes.Evo.route) },
            icon = { Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = stringResource(R.string.nav_evo)) },
            label = { Text(stringResource(R.string.nav_evo)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedTextColor = MaterialTheme.colorScheme.secondary,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == NavRoutes.Profile.route,
            onClick = { onNavigate(NavRoutes.Profile.route) },
            icon = { Icon(Icons.Default.Person, contentDescription = stringResource(R.string.nav_profile)) },
            label = { Text(stringResource(R.string.nav_profile)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedTextColor = MaterialTheme.colorScheme.secondary,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun EvoFitButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeightPrimary),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(Dimens.CornerRadiusSmall)
    ) {
        Text(
            text = text,
            color = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarReturn(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    subtitle: String? = null,
    isCenterAligned: Boolean = true,
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
@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun TopBarReturnPreview() {
    EvoFitTheme {
        TopBarReturn(
            title = "Título da Tela",
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun TopBarReturnNoTitlePreview() {
    EvoFitTheme {
        TopBarReturn(
            onBackClick = {}
        )
    }
}

@Composable
fun EvoFitAlertDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String? = null,
    icon: ImageVector = Icons.Default.Cancel,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        EvoFitAlertDialogContent(
            title = title,
            description = description,
            confirmButtonText = confirmButtonText,
            dismissButtonText = dismissButtonText,
            icon = icon,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun EvoFitAlertDialogContent(
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String? = null,
    icon: ImageVector = Icons.Default.Cancel,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLarge),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.MinimumTouchTarget)
                    .background(MaterialTheme.colorScheme.errorContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(Dimens.IconSizeDefault)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = description,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.MinimumTouchTarget),
                shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(
                    text = confirmButtonText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (dismissButtonText != null) {
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.MinimumTouchTarget),
                    shape = RoundedCornerShape(Dimens.CornerRadiusSmall)
                ) {
                    Text(
                        text = dismissButtonText,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun EvoFitCautionDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        EvoFitCautionDialogContent(
            title = title,
            description = description,
            confirmButtonText = confirmButtonText,
            dismissButtonText = dismissButtonText,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun EvoFitCautionDialogContent(
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLarge),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.MinimumTouchTarget)
                    .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(Dimens.IconSizeDefault)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = description,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.MinimumTouchTarget),
                shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(
                    text = confirmButtonText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (dismissButtonText != null) {
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.MinimumTouchTarget),
                    shape = RoundedCornerShape(Dimens.CornerRadiusSmall)
                ) {
                    Text(
                        text = dismissButtonText,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun EvoFitActionDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String? = null,
    icon: ImageVector = Icons.Outlined.CheckCircle,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        EvoFitActionDialogContent(
            title = title,
            description = description,
            confirmButtonText = confirmButtonText,
            dismissButtonText = dismissButtonText,
            icon = icon,
            onConfirm = onConfirm,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun EvoFitActionDialogContent(
    title: String,
    description: String,
    confirmButtonText: String,
    dismissButtonText: String? = null,
    icon: ImageVector = Icons.Outlined.CheckCircle,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.SpacingMedium),
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLarge),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(Dimens.MinimumTouchTarget)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimens.IconSizeDefault)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

            Text(
                text = description,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.MinimumTouchTarget),
                shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = confirmButtonText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            if (dismissButtonText != null) {
                Spacer(modifier = Modifier.height(Dimens.SpacingSmall))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.MinimumTouchTarget),
                    shape = RoundedCornerShape(Dimens.CornerRadiusSmall)
                ) {
                    Text(
                        text = dismissButtonText,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun AppBottomNavigationPreview() {
    EvoFitTheme {
        AppBottomNavigation(
            currentRoute = "home",
            onNavigate = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun EvoFitConfirmationDialogPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EvoFitAlertDialogContent(
                title = "Excluir treino?",
                description = "Costas predio e seus 3 exercícios serão apagados. Essa ação não pode ser desfeita.",
                confirmButtonText = "Excluir treino",
                dismissButtonText = "Cancelar",
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun EvoFitConfirmationDialogNoDismissPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EvoFitAlertDialogContent(
                title = "Ação Obrigatória",
                description = "Você precisa confirmar esta ação para continuar.",
                confirmButtonText = "Entendido",
                onConfirm = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun EvoFitConfirmationActionDialogPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EvoFitActionDialogContent(
                title = "Concluir treino?",
                description = "Seu progresso será salvo e você poderá ver as estatísticas depois.",
                confirmButtonText = "Concluir",
                dismissButtonText = "Continuar treinando",
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun EvoFitCautionDialogPreview() {
    EvoFitTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            EvoFitCautionDialogContent(
                title = "Atenção!",
                description = "Você está prestes a sair sem salvar. Algumas alterações podem ser perdidas.",
                confirmButtonText = "Salvar e Sair",
                dismissButtonText = "Cancelar",
                onConfirm = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun DialogsGalleryPreview() {
    EvoFitTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.SpacingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimens.SpacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Galeria de Diálogos", color = Color.White, style = MaterialTheme.typography.titleMedium)
            
            EvoFitAlertDialogContent(
                title = "Diálogo de Alerta (Erro)",
                description = "Este é o estilo usado para ações críticas.",
                confirmButtonText = "Confirmar",
                dismissButtonText = "Cancelar",
                onConfirm = {}
            )

            EvoFitCautionDialogContent(
                title = "Diálogo de Cuidado",
                description = "Este é o estilo usado para avisos importantes.",
                confirmButtonText = "Entendi",
                onConfirm = {}
            )

            EvoFitActionDialogContent(
                title = "Diálogo de Sucesso",
                description = "Este é o estilo usado para conclusões positivas.",
                confirmButtonText = "Concluir",
                onConfirm = {}
            )
        }
    }
}

@Composable
fun EvoFitDropdownFilter(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    options: List<String> = listOf("1 mês", "3 meses", "6 meses", "Tudo"),
    initialExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(initialExpanded) }

    // Rotação suave do ícone de seta (sobe quando aberto, desce quando fechado)
    val arrowRotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "arrowRotation"
    )

    // O Box externo serve como a âncora de posicionamento para o DropdownMenu
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(Dimens.CornerRadiusCardSmall))
                .background(MaterialTheme.colorScheme.surface)
                .border(Dimens.BorderWidthThin, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(Dimens.CornerRadiusCardSmall))
                .clickable { isExpanded = !isExpanded }
                .padding(horizontal = Dimens.SpacingMedium, vertical = Dimens.SpacingSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingTiny)
        ) {
            Text(
                text = selectedOption,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(Dimens.IconSizeMedium)
                    .rotate(arrowRotationState)
            )
        }

        // O Menu Suspenso
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            offset = DpOffset(x = Dimens.SpacingNone, y = Dimens.SpacingSmall),
            shape = RoundedCornerShape(Dimens.CornerRadiusCardSmall),
            modifier = Modifier
                .width(Dimens.DropdownMenuWidth)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(Dimens.CornerRadiusCardSmall))
                .border(Dimens.BorderWidthThin, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(Dimens.CornerRadiusCardSmall))
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption

                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        isExpanded = false
                    },
                    contentPadding = PaddingValues(vertical = Dimens.SpacingMediumSmall)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun EvoFitDropdownFilterPreview() {
    EvoFitTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SectionSpacing),
            contentAlignment = Alignment.Center
        ) {
            var selected by remember { mutableStateOf("3 meses") }
            EvoFitDropdownFilter(
                selectedOption = selected,
                onOptionSelected = { selected = it }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun EvoFitDropdownFilterExpandedPreview() {
    EvoFitTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.PreviewHeightLarge) // Aumentado para garantir visibilidade do menu
                .padding(Dimens.SpacingMedium),
            contentAlignment = Alignment.TopCenter
        ) {
            // No Preview, o DropdownMenu às vezes precisa de um container para ser renderizado corretamente
            EvoFitDropdownFilter(
                selectedOption = "3 meses",
                onOptionSelected = {},
                initialExpanded = true
            )
        }
    }
}

@Composable
fun EvoFitInputField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth().heightIn(min = Dimens.TextFieldHeight),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        label = { Text(label) },
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)) },
        trailingIcon = trailingIcon,
        enabled = enabled,
        singleLine = true,
        shape = RoundedCornerShape(Dimens.CornerRadiusExtraSmall),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledTextColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        ),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
private fun EvoFitInputFieldPreview() {
    EvoFitTheme {
        Column(modifier = Modifier.padding(Dimens.SpacingMedium)) {
            EvoFitInputField(
                label = "Nome",
                placeholder = "Digite seu nome",
                value = "",
                onValueChange = {}
            )
            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
            EvoFitInputField(
                label = "E-mail",
                placeholder = "exemplo@email.com",
                value = "usuario@evofit.com",
                onValueChange = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF090909)
@Composable
fun EvoFitButtonPreview() {
    EvoFitTheme {
        EvoFitButton(
            text = "Continuar",
            onClick = {}
        )
    }
}
