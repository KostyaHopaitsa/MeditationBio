package com.example.meditationbiorefactoring.feature_bio.presentation.bio_history

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.feature_bio.domain.use_case.GetMeasurementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class BioHistoryViewModel @Inject constructor(
    private val getMeasurementsUseCase: GetMeasurementsUseCase
): ViewModel() {

    private val _state = mutableStateOf(BioHistoryState())
    val state:State<BioHistoryState> = _state

    init {
        _state.value = _state.value.copy(isLoading = true)

        getMeasurementsUseCase()
            .onEach { measurements ->
                _state.value = _state.value.copy(
                    measurements = measurements,
                    isLoading = false,
                    error = null
                )
            }
            .catch { e ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
            .launchIn(viewModelScope)
    }
}