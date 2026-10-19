package com.guaracode.evofit.presentation.ui.feature.profile.home.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.guaracode.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.guaracode.evofit.presentation.ui.feature.components.EvoFitAlertDialog
import com.guaracode.evofit.presentation.ui.feature.profile.home.components.ImageSourcePickerBottomSheet
import com.guaracode.evofit.presentation.ui.feature.profile.home.components.LogoutComponent
import com.guaracode.evofit.presentation.ui.feature.profile.home.components.ProfileMenuItemData
import com.guaracode.evofit.presentation.ui.feature.profile.home.components.ProfileOptionsMenuComponent
import com.guaracode.evofit.presentation.ui.feature.profile.home.components.ProfilePhotoExpandedDialog
import com.guaracode.evofit.presentation.ui.feature.profile.home.components.UserDataInfoComponent
import com.guaracode.evofit.presentation.ui.feature.profile.home.tracking.ProfileHomeTracker
import com.guaracode.evofit.presentation.ui.feature.profile.home.viewmodel.ProfileViewModel
import com.guaracode.evofit.presentation.ui.theme.Dimens
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.logout_dialog_confirm
import evofit.shared.generated.resources.logout_dialog_message
import evofit.shared.generated.resources.logout_dialog_title
import evofit.shared.generated.resources.nav_profile
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileHomeScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    tracker: ProfileHomeTracker = koinInject(),
    onNavigate: (String) -> Unit = {},
    onUserDataClick: () -> Unit = {},
    onGoalsClick: () -> Unit = {},
    onLogoutSuccess: () -> Unit = {},
    isDeveloperMenuEnabled: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        tracker.trackProfileHomeScreenView()
    }
    
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isPhotoExpanded by remember { mutableStateOf(false) }
    var showImageSourcePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onLogoutSuccess()
        }
    }

    ProfileHomeScreenContent(
        userName = uiState.name,
        userWeight = uiState.weight,
        userHeight = uiState.height.ifEmpty { "--" },
        profileImageUrl = uiState.profilePictureUri ?: "",
        isDeveloperMenuEnabled = isDeveloperMenuEnabled,
        onNavigate = onNavigate,
        onUserDataClick = onUserDataClick,
        onGoalsClick = onGoalsClick,
        onSupportClick = { onNavigate("support") },
        onAboutClick = { onNavigate("about") },
        onDeveloperClick = { onNavigate("developer") },
        onLogoutClick = { showLogoutDialog = true },
        onImageClick = { isPhotoExpanded = true }
    )

    if (isPhotoExpanded) {
        ProfilePhotoExpandedDialog(
            profileImageUrl = uiState.profilePictureUri ?: "",
            onDismiss = { isPhotoExpanded = false },
            onEditClick = {
                isPhotoExpanded = false
                showImageSourcePicker = true
            }
        )
    }

    if (showImageSourcePicker) {
        ImageSourcePickerBottomSheet(
            onDismiss = { showImageSourcePicker = false },
            onGalleryClick = { },
            onCameraClick = { }
        )
    }

    if (showLogoutDialog) {
        EvoFitAlertDialog(
            title = stringResource(Res.string.logout_dialog_title),
            description = stringResource(Res.string.logout_dialog_message),
            confirmButtonText = stringResource(Res.string.logout_dialog_confirm),
            dismissButtonText = stringResource(Res.string.logout_dialog_confirm),
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onConfirm = {
                showLogoutDialog = false
                viewModel.logout()
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

@Composable
fun ProfileHomeScreenContent(
    modifier: Modifier = Modifier,
    userName: String,
    userWeight: String,
    userHeight: String,
    profileImageUrl: String,
    isDeveloperMenuEnabled: Boolean = false,
    onNavigate: (String) -> Unit,
    onUserDataClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onDeveloperClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onImageClick: () -> Unit,
    onSupportClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val menuItems = remember(onUserDataClick, onGoalsClick, onDeveloperClick, onSupportClick, onAboutClick, isDeveloperMenuEnabled) {
        val baseList = mutableListOf(
            ProfileMenuItemData("1", "Dados do Usuário", Icons.Default.Person, isEnabled = true, isVisible = true, onUserDataClick),
            ProfileMenuItemData("2", "Metas Pessoais", Icons.Default.Person, isEnabled = true, isVisible = true, onGoalsClick),
            ProfileMenuItemData("3", "Preferências", Icons.Default.Settings, isEnabled = false, isVisible = false, {}),
            ProfileMenuItemData("4", "Notificações", Icons.Default.Notifications, isEnabled = false, isVisible = false, {}),
            ProfileMenuItemData("5", "Ajuda e Suporte", Icons.AutoMirrored.Filled.HelpOutline, isEnabled = true, isVisible = true, onSupportClick),
            ProfileMenuItemData("6", "Sobre o App", Icons.Default.Info, isEnabled = true, isVisible = true, onAboutClick)
        )

        if (isDeveloperMenuEnabled) {
            baseList.add(
                ProfileMenuItemData("7", "Menu do Desenvolvedor", Icons.Default.BugReport, isEnabled = true, isVisible = true, onDeveloperClick)
            )
        }

        baseList.toList()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomNavigation(
                currentRoute = "profile",
                onNavigate = onNavigate
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Dimens.ScreenPaddingHorizontal)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Dimens.SectionSpacing)
        ) {
            Text(
                text = stringResource(Res.string.nav_profile),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = Dimens.SpacingMedium)
            )
            
            UserDataInfoComponent(
                userName = userName,
                weight = "$userWeight kg",
                height = "$userHeight m",
                profileImageUrl = profileImageUrl,
                onImageClick = onImageClick
            )

            ProfileOptionsMenuComponent(
                items = menuItems
            )

            LogoutComponent(
                onLogoutClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(Dimens.SpacingMedium))
        }
    }
}

@Preview
@Composable
private fun ProfileHomeScreenPreview() {
    EvoFitTheme {
        ProfileHomeScreenContent(
            userName = "Julia",
            userWeight = "54",
            userHeight = "1.65",
            profileImageUrl = "",
            onNavigate = {},
            onUserDataClick = {},
            onGoalsClick = {},
            onDeveloperClick = {},
            onLogoutClick = {},
            onImageClick = {},
            onSupportClick = {},
            onAboutClick = {}
        )
    }
}
