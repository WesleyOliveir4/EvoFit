package com.guaracode.evofit.presentation.ui.feature.profile.userdata.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guaracode.evofit.presentation.ui.feature.components.EvoDatePickerDialog
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.components.EvoWeightPickerDialog
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.components.UserDataItem
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.tracking.UserDataTracker
import com.guaracode.evofit.presentation.ui.feature.profile.userdata.viewmodel.UserDataViewModel
import com.guaracode.evofit.presentation.ui.theme.EvoFitTheme
import com.guaracode.evofit.presentation.ui.theme.evoColors
import evofit.shared.generated.resources.Res
import evofit.shared.generated.resources.new_workout_back_desc
import evofit.shared.generated.resources.onboarding_user_data_label_birth_date
import evofit.shared.generated.resources.onboarding_user_data_label_name
import evofit.shared.generated.resources.onboarding_user_data_label_weight
import evofit.shared.generated.resources.profile_user_data
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserDataScreen(
    viewModel: UserDataViewModel = koinViewModel(),
    tracker: UserDataTracker = koinInject(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        tracker.trackUserDataScreenView()
    }

    UserDataScreenContent(
        nameValue = uiState.name,
        birthDateValue = uiState.birthDate,
        weightValue = uiState.weight,
        isLoading = uiState.isLoading,
        onBackClick = onBackClick,
        onUpdateName = { viewModel.updateUserData(name = it) },
        onUpdateBirthDate = { viewModel.updateUserData(birthDate = it) },
        onUpdateWeight = { viewModel.updateUserData(weight = it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataScreenContent(
    modifier: Modifier = Modifier,
    nameValue: String,
    birthDateValue: String,
    weightValue: String,
    isLoading: Boolean = false,
    onBackClick: () -> Unit,
    onUpdateName: (String) -> Unit,
    onUpdateBirthDate: (String) -> Unit,
    onUpdateWeight: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showWeightPicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        EvoDatePickerDialog(
            initialDate = birthDateValue,
            onDismiss = { showDatePicker = false },
            onConfirm = onUpdateBirthDate
        )
    }

    if (showWeightPicker) {
        val initialWeight = weightValue.filter { it.isDigit() }.toIntOrNull() ?: 70
        EvoWeightPickerDialog(
            initialWeight = initialWeight,
            onDismiss = { showWeightPicker = false },
            onConfirm = {
                onUpdateWeight(it.toString())
                showWeightPicker = false
            }
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.profile_user_data),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.new_workout_back_desc),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                UserDataItem(
                    icon = Icons.Default.Person,
                    title = stringResource(Res.string.onboarding_user_data_label_name),
                    value = nameValue,
                    onValueChangeConfirmed = onUpdateName
                )

                UserDataItem(
                    icon = Icons.Default.DateRange,
                    title = stringResource(Res.string.onboarding_user_data_label_birth_date),
                    value = birthDateValue,
                    customEditAction = { showDatePicker = true }
                )

                UserDataItem(
                    icon = Icons.Default.MonitorWeight,
                    title = stringResource(Res.string.onboarding_user_data_label_weight),
                    value = if (weightValue.isNotEmpty()) "$weightValue kg" else "",
                    customEditAction = { showWeightPicker = true }
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.evoColors.green
                )
            }
        }
    }
}

@Preview
@Composable
private fun UserDataScreenPreview() {
    EvoFitTheme {
        UserDataScreenContent(
            nameValue = "Wesley",
            birthDateValue = "27/07/1995",
            weightValue = "78",
            onBackClick = {},
            onUpdateName = {},
            onUpdateBirthDate = {},
            onUpdateWeight = {}
        )
    }
}
