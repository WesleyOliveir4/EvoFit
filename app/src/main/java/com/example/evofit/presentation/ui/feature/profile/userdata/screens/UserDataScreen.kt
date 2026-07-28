package com.example.evofit.presentation.ui.feature.profile.userdata.screens

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.components.EvoDatePickerDialog
import com.example.evofit.presentation.ui.feature.profile.userdata.components.EvoWeightPickerDialog
import com.example.evofit.presentation.ui.feature.profile.userdata.components.UserDataItem
import com.example.evofit.presentation.ui.feature.profile.userdata.viewmodel.UserDataViewModel
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.AppGreen
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextPrimary
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDataScreen(
    viewModel: UserDataViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

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
        containerColor = AppDarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.profile_user_data),
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.new_workout_back_desc),
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppDarkBg)
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
                    title = stringResource(id = R.string.onboarding_user_data_label_name),
                    value = nameValue,
                    onValueChangeConfirmed = onUpdateName
                )

                UserDataItem(
                    icon = Icons.Default.DateRange,
                    title = stringResource(id = R.string.onboarding_user_data_label_birth_date),
                    value = birthDateValue,
                    customEditAction = { showDatePicker = true }
                )

                UserDataItem(
                    icon = Icons.Default.MonitorWeight,
                    title = stringResource(id = R.string.onboarding_user_data_label_weight),
                    value = if (weightValue.isNotEmpty()) "$weightValue kg" else "",
                    customEditAction = { showWeightPicker = true }
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppGreen
                )
            }
        }
    }
}

@Preview(showBackground = true)
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
