package com.example.evofit.presentation.ui.feature.profile.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evofit.domain.usecase.GetOnboardingDataUseCase
import com.example.evofit.domain.usecase.LogoutUseCase
import com.example.evofit.domain.usecase.NukeUserDataUseCase
import com.example.evofit.domain.usecase.SaveOnboardingDataUseCase
import com.example.evofit.domain.model.UserOnboardingData
import com.example.evofit.presentation.ui.feature.profile.home.state.ProfileUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val getOnboardingDataUseCase: GetOnboardingDataUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val nukeUserDataUseCase: NukeUserDataUseCase,
    private val saveOnboardingDataUseCase: SaveOnboardingDataUseCase
) : ViewModel() {

    private val _isLoggedOut = MutableStateFlow(false)

    val uiState: StateFlow<ProfileUiState> = _isLoggedOut
        .flatMapLatest { isLoggedOut ->
            if (isLoggedOut) {
                flow { emit(ProfileUiState(isLoggedOut = true)) }
            } else {
                getOnboardingDataUseCase().map { userData ->
                    ProfileUiState(
                        name = getFirstName(userData.name),
                        birthDate = userData.birthDate,
                        weight = userData.weight,
                        height = userData.height,
                        profilePictureUri = userData.profilePictureUri,
                        isLoading = false
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = ProfileUiState(isLoading = true)
        )

    private fun getFirstName(fullName: String): String {
        return fullName.trim().split("\\s+".toRegex()).firstOrNull() ?: fullName
    }

    fun updateProfilePicture(uri: String) {
        viewModelScope.launch {
            val currentData = getOnboardingDataUseCase().first()
            val updatedData = currentData.copy(profilePictureUri = uri)
            saveOnboardingDataUseCase(updatedData)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase().onSuccess {
                nukeUserDataUseCase()
                _isLoggedOut.value = true
            }
        }
    }
}
