package com.example.evofit.presentation.ui.feature.profile.userdata.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evofit.R
import com.example.evofit.presentation.ui.feature.profile.userdata.components.FormInputField
import com.example.evofit.presentation.ui.feature.profile.userdata.viewmodel.UserDataViewModel
import com.example.evofit.presentation.ui.theme.AppDarkBg
import com.example.evofit.presentation.ui.theme.AppGreen
import com.example.evofit.presentation.ui.theme.EvoFitTheme
import com.example.evofit.presentation.ui.theme.TextPrimary
import com.example.evofit.presentation.ui.theme.TextSecondary
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDataScreen(
    viewModel: UserDataViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.resetSavedState()
        }
    }

    UserDataScreenContent(
        nameValue = uiState.name,
        ageValue = uiState.age,
        weightValue = uiState.weight,
        isLoading = uiState.isLoading,
        onBackClick = onBackClick,
        onSaveClick = { name, age, weight ->
            viewModel.updateUserData(name, age, weight)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataScreenContent(
    modifier: Modifier = Modifier,
    nameValue: String,
    ageValue: String,
    weightValue: String,
    isLoading: Boolean = false,
    onBackClick: () -> Unit,
    onSaveClick: (name: String, age: String, weight: String) -> Unit
) {
    var name by remember(nameValue) { mutableStateOf(nameValue) }
    var age by remember(ageValue) { mutableStateOf(ageValue) }
    var weight by remember(weightValue) { mutableStateOf(weightValue) }
    var isEditing by remember { mutableStateOf(false) }

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
                actions = {
                    IconButton(onClick = { isEditing = !isEditing }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(id = R.string.user_data_edit_desc),
                            tint = if (isEditing) TextSecondary else AppGreen
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
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                FormInputField(
                    label = stringResource(id = R.string.onboarding_user_data_label_name).uppercase(),
                    value = name,
                    onValueChange = { name = it },
                    enabled = isEditing
                )

                FormInputField(
                    label = stringResource(id = R.string.onboarding_user_data_label_age).uppercase(),
                    value = age,
                    onValueChange = { age = it },
                    enabled = isEditing,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                FormInputField(
                    label = stringResource(id = R.string.onboarding_user_data_label_weight).uppercase(),
                    value = weight,
                    onValueChange = { weight = it },
                    enabled = isEditing,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(100.dp))
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = AppGreen
                )
            }

            AnimatedVisibility(
                visible = isEditing,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        isEditing = false
                        onSaveClick(name, age, weight)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppGreen,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.user_data_update_button),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
            ageValue = "28",
            weightValue = "78",
            onBackClick = {},
            onSaveClick = { _, _, _ -> }
        )
    }
}