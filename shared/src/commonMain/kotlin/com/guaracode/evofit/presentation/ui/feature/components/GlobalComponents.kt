package com.guaracode.evofit.presentation.ui.feature.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_logo_evofit
import org.jetbrains.compose.resources.painterResource

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
            selected = currentRoute == "home",
            onClick = { onNavigate("home") },
            icon = { Icon(painterResource(Res.drawable.ic_logo_evofit), contentDescription = "Treino") },
            label = { Text("Treino") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedTextColor = MaterialTheme.colorScheme.secondary,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "evo",
            onClick = { onNavigate("evo") },
            icon = { Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = "Evo") },
            label = { Text("Evo") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.secondary,
                unselectedTextColor = MaterialTheme.colorScheme.secondary,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
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

@Preview
@Composable
private fun TopBarReturnPreview() {
    EvoFitTheme {
        TopBarReturn(
            title = "Título da Tela",
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

@Composable
fun EvoFitDropdownFilter(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    options: List<String> = listOf("1 mês", "3 meses", "6 meses", "Tudo"),
    initialExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(initialExpanded) }

    val arrowRotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "arrowRotation"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(Dimens.CornerRadiusCardSmall))
                .background(MaterialTheme.colorScheme.surface)
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
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
            cursorColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.background,
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
