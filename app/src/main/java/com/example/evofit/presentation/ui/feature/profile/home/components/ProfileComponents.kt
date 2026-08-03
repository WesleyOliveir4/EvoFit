package com.example.evofit.presentation.ui.feature.profile.home.components

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
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme

// Estrutura de dados para os itens do menu
data class ProfileMenuItemData(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

/**
 * 1. Bloco de Informações do Usuário (Foto com borda verde + Card com Nome, Peso e Altura)
 */
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
        // Card de fundo (com padding superior para acomodar a foto sobreposta)
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
                    .padding(top = 52.dp, bottom = Dimens.SpacingLarge, start = Dimens.SpacingLarge, end = Dimens.SpacingLarge),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Linha com as estatísticas de Peso e Altura
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Coluna Peso
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🏋️", fontSize = Dimens.TextSizeLarge) 
                        Spacer(modifier = Modifier.height(Dimens.SpacingExtraSmall))
                        Text(
                            text = weight, 
                            color = MaterialTheme.colorScheme.onSurface, 
                            fontSize = Dimens.TextSizeMedium, 
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Peso", 
                            color = MaterialTheme.colorScheme.secondary, 
                            fontSize = Dimens.TextSizeExtraSmall
                        )
                    }

                    // Nome Centralizado
                    Text(
                        text = userName,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = Dimens.TextSizeExtraLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Coluna Altura
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "↕️", fontSize = Dimens.TextSizeLarge)
                        Spacer(modifier = Modifier.height(Dimens.SpacingExtraSmall))
                        Text(
                            text = height, 
                            color = MaterialTheme.colorScheme.onSurface, 
                            fontSize = Dimens.TextSizeMedium, 
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Altura", 
                            color = MaterialTheme.colorScheme.secondary, 
                            fontSize = Dimens.TextSizeExtraSmall
                        )
                    }
                }
            }
        }

        // Foto de perfil circular sobreposta com borda verde
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
            if (profileImageUrl.isNotEmpty()) {
                AsyncImage(
                    model = profileImageUrl,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxSize(0.6f)
                )
            }
        }
    }
}

/**
 * Diálogo para exibição da foto de perfil expandida
 */
@Composable
fun ProfilePhotoExpandedDialog(
    profileImageUrl: String,
    onDismiss: () -> Unit,
    onEditClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Imagem Centralizada
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.SpacingLarge),
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = profileImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }

            // Barra Superior de Ações
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

/**
 * BottomSheet para escolha da fonte da imagem (Câmera ou Galeria)
 */
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

/**
 * 2. Bloco do Menu de Opções ("MINHA CONTA")
 */
@Composable
fun ProfileOptionsMenuComponent(
    items: List<ProfileMenuItemData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall)
    ) {
        Text(
            text = "MINHA CONTA",
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
                items.forEachIndexed { index, item ->
                    ProfileOptionRow(
                        title = item.title,
                        icon = item.icon,
                        onClick = item.onClick
                    )

                    // Divisória horizontal entre os itens (menos no último)
                    if (index < items.size - 1) {
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

// Item reutilizável da lista do menu
@Composable
private fun ProfileOptionRow(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onClick() }
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

/**
 * 3. Bloco de Logout ("SAIR")
 */
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
            text = "SAIR",
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
                    text = "Sair da Conta",
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

@Preview(showBackground = true)
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
                    ProfileMenuItemData("1", "Dados do Usuário", Icons.Default.Person, {}),
                    ProfileMenuItemData("2", "Metas Pessoais", Icons.Default.Star, {})
                )
            )

            LogoutComponent(onLogoutClick = {})
        }
    }
}
