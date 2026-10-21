package com.guaracode.evofit.presentation.ui.feature.profile.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import com.guaracode.evofit.presentation.ui.theme.evoColors
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.ic_balance
import evofit.shared.generated.resources.ic_ruller
import evofit.shared.generated.resources.onboarding_summary_label_height
import evofit.shared.generated.resources.onboarding_user_data_label_weight
import evofit.shared.generated.resources.profile_logout
import evofit.shared.generated.resources.profile_my_account
import evofit.shared.generated.resources.profile_section_logout
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

data class ProfileMenuItemData(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val isEnabled: Boolean = true,
    val isVisible: Boolean = true,
    val onClick: () -> Unit
)

@Composable
fun UserDataInfoComponent(
    userName: String,
    weight: String,
    height: String,
    profileImageUrl: String,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp),
            shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 52.dp, bottom = Dimens.SpacingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_balance),
                            contentDescription = null,
                            tint = MaterialTheme.evoColors.green,
                            modifier = Modifier.size(Dimens.IconSizeSmall)
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpacingExtraSmall))
                        Text(
                            text = weight, 
                            color = MaterialTheme.colorScheme.onSurface, 
                            fontSize = Dimens.TextSizeMedium, 
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(Res.string.onboarding_user_data_label_weight), 
                            color = MaterialTheme.colorScheme.secondary, 
                            fontSize = Dimens.TextSizeExtraSmall
                        )
                    }

                    Text(
                        text = userName,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = Dimens.TextSizeExtraLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_ruller),
                            contentDescription = null,
                            tint = MaterialTheme.evoColors.green,
                            modifier = Modifier.size(Dimens.IconSizeSmall)
                        )
                        Spacer(modifier = Modifier.height(Dimens.SpacingExtraSmall))
                        Text(
                            text = height, 
                            color = MaterialTheme.colorScheme.onSurface, 
                            fontSize = Dimens.TextSizeMedium, 
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(Res.string.onboarding_summary_label_height), 
                            color = MaterialTheme.colorScheme.secondary, 
                            fontSize = Dimens.TextSizeExtraSmall
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .size(88.dp)
                .border(2.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de perfil",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxSize(0.6f)
            )
        }
    }
}

@Composable
fun ProfilePhotoExpandedDialog(
    profileImageUrl: String,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.SpacingLarge),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(200.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = Color.White
                    )
                }

                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar foto",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSourcePickerBottomSheet(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.SpacingLarge)
        ) {
            Text(
                text = "Foto de Perfil",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = Dimens.SpacingMedium)
            )

            ListItem(
                headlineContent = { Text("Câmera") },
                leadingContent = {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                },
                modifier = Modifier.clickable { 
                    onCameraClick()
                    onDismiss()
                }
            )

            ListItem(
                headlineContent = { Text("Galeria") },
                leadingContent = {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                },
                modifier = Modifier.clickable { 
                    onGalleryClick()
                    onDismiss()
                }
            )
            
            Spacer(modifier = Modifier.height(Dimens.SpacingLarge))
        }
    }
}

@Composable
fun ProfileOptionsMenuComponent(
    items: List<ProfileMenuItemData>,
    modifier: Modifier = Modifier
) {
    val visibleItems = items.filter { it.isVisible }
    if (visibleItems.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Text(
            text = stringResource(Res.string.profile_my_account),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = Dimens.TextSizeTiny,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = Dimens.SpacingSmall)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.CornerRadiusCard),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                visibleItems.forEachIndexed { index, item ->
                    ProfileOptionRow(
                        title = item.title,
                        icon = item.icon,
                        isEnabled = item.isEnabled,
                        onClick = item.onClick
                    )

                    if (index < visibleItems.size - 1) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = Dimens.BorderWidthThin,
                            modifier = Modifier.padding(start = 68.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileOptionRow(
    title: String,
    icon: ImageVector,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val contentAlpha = if (isEnabled) 1f else 0.4f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .alpha(contentAlpha)
            .clickable(enabled = isEnabled) { onClick() }
            .padding(horizontal = Dimens.SpacingMedium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
    ) {
        Card(
            shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Box(
                modifier = Modifier.size(38.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimens.IconSizeSmall)
                )
            }
        }

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = Dimens.TextSizeMediumSmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(Dimens.IconSizeMediumSmall)
        )
    }
}

@Composable
fun LogoutComponent(
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Text(
            text = stringResource(Res.string.profile_section_logout),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = Dimens.TextSizeTiny,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(horizontal = Dimens.SpacingSmall)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.CornerRadiusCard))
                .clickable { onLogoutClick() },
            shape = RoundedCornerShape(Dimens.CornerRadiusCard),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = Dimens.SpacingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium)
            ) {
                Card(
                    shape = RoundedCornerShape(Dimens.CornerRadiusSmall),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Box(
                        modifier = Modifier.size(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(Dimens.IconSizeSmall)
                        )
                    }
                }

                Text(
                    text = stringResource(Res.string.profile_logout),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = Dimens.TextSizeMediumSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(Dimens.IconSizeMediumSmall)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileComponentsPreview() {
    EvoFitTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            UserDataInfoComponent(
                userName = "Julia",
                weight = "54 kg",
                height = "1,65 m",
                profileImageUrl = "",
                onImageClick = {}
            )

            ProfileOptionsMenuComponent(
                items = listOf(
                    ProfileMenuItemData("1", "Habilitado e Visível", Icons.Default.Person, isEnabled = true, isVisible = true, {}),
                    ProfileMenuItemData("2", "Desabilitado e Visível", Icons.Default.Star, isEnabled = false, isVisible = true, {}),
                    ProfileMenuItemData("3", "Oculto (não aparecerá abaixo)", Icons.Default.Settings, isEnabled = false, isVisible = false, {})
                )
            )

            LogoutComponent(onLogoutClick = {})
        }
    }
}
