package com.example.evofit.presentation.ui.feature.profile.home.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.evofit.R
import com.example.evofit.navigation.NavRoutes
import com.example.evofit.presentation.ui.feature.components.AppBottomNavigation
import com.example.evofit.presentation.ui.feature.components.EvoFitAlertDialogContent
import com.example.evofit.BuildConfig
import com.example.evofit.presentation.ui.feature.profile.home.components.*
import com.example.evofit.presentation.ui.feature.profile.home.viewmodel.ProfileViewModel
import com.example.evofit.presentation.ui.theme.Dimens
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun ProfileHomeScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onNavigate: (String) -> Unit = {},
    onUserDataClick: () -> Unit = {},
    onGoalsClick: () -> Unit = {},
    onLogoutSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isPhotoExpanded by remember { mutableStateOf(false) }
    var showImageSourcePicker by remember { mutableStateOf(false) }

    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.updateProfilePicture(it.toString()) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempPhotoUri?.let { viewModel.updateProfilePicture(it.toString()) }
        }
    }

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
        onNavigate = onNavigate,
        onUserDataClick = onUserDataClick,
        onGoalsClick = onGoalsClick,
        onDeveloperClick = { onNavigate(NavRoutes.Developer.route) },
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
            onGalleryClick = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            onCameraClick = {
                val file = File(context.cacheDir, "images/profile_temp.jpg").apply {
                    parentFile?.mkdirs()
                }
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                tempPhotoUri = uri
                cameraLauncher.launch(uri)
            }
        )
    }

    if (showLogoutDialog) {
        EvoFitAlertDialogContent(
            title = stringResource(id = R.string.logout_dialog_title),
            description = stringResource(id = R.string.logout_dialog_message),
            confirmButtonText = stringResource(id = R.string.logout_dialog_confirm),
            dismissButtonText = stringResource(id = R.string.logout_dialog_cancel),
            icon = ImageVector.vectorResource(id = R.drawable.ic_logout),
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
    onNavigate: (String) -> Unit,
    onUserDataClick: () -> Unit,
    onGoalsClick: () -> Unit,
    onDeveloperClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onImageClick: () -> Unit
) {
    val medalIcon = ImageVector.vectorResource(id = R.drawable.ic_medal)

    val menuItems = remember(onUserDataClick, onGoalsClick, medalIcon, onDeveloperClick) {
        val baseList = mutableListOf(
            ProfileMenuItemData("1", "Dados do Usuário", Icons.Default.Person, isEnabled = true, isVisible = true, onUserDataClick),
            ProfileMenuItemData("2", "Metas Pessoais", medalIcon, isEnabled = true, isVisible = true, onGoalsClick),
            ProfileMenuItemData("3", "Preferências", Icons.Default.Settings, isEnabled = false, isVisible = false, {}),
            ProfileMenuItemData("4", "Notificações", Icons.Default.Notifications, isEnabled = false, isVisible = false, {}),
            ProfileMenuItemData("5", "Ajuda e Suporte", Icons.AutoMirrored.Filled.HelpOutline, isEnabled = false, isVisible = true, {}),
            ProfileMenuItemData("6", "Sobre o App", Icons.Default.Info, isEnabled = false, isVisible = true, {})
        )

        if (BuildConfig.FLAVOR == "staging" && BuildConfig.DEBUG) {
            baseList.add(
                ProfileMenuItemData("7", "Menu do Desenvolvedor", Icons.Default.BugReport, isEnabled = true, isVisible = true, onDeveloperClick)
            )
        }

        baseList.toList()
    }

    Scaffold(
        modifier = modifier,
        containerColor = com.example.evofit.presentation.ui.theme.AppDarkBg,
        bottomBar = {
            AppBottomNavigation(
                currentRoute = NavRoutes.Profile.route,
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
                text = stringResource(id = R.string.nav_profile),
                color = com.example.evofit.presentation.ui.theme.TextPrimary,
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

@Preview(showBackground = true)
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
            onImageClick = {}
        )
    }
}
