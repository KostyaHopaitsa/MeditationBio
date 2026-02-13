package com.example.meditationbiorefactoring.bio.presentation.bio_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.bio.domain.use_case.measurement_use_case.DeleteMeasurementUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.measurement_use_case.GetMeasurementsUseCase
import com.example.meditationbiorefactoring.common.DomainResult
import com.example.meditationbiorefactoring.common.toUiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BioHistoryViewModel @Inject constructor(
    private val getMeasurementsUseCase: GetMeasurementsUseCase,
    private val deleteMeasurementUseCase: DeleteMeasurementUseCase
): ViewModel() {

    private val _state = MutableStateFlow(BioHistoryState())
    val state: StateFlow<BioHistoryState> = _state
    private val _navigateEvent = Channel<String>(Channel.BUFFERED)
    val navigateEvent = _navigateEvent.receiveAsFlow()

    init {
        _state.value = _state.value.copy(isLoading = true)

        getMeasurementsUseCase()
            .onEach { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _state.value = _state.value.copy(
                            measurements = result.data,
                            isLoading = false,
                            error = ""
                        )
                    }
                    is DomainResult.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = (result.error.toUiError().message)
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: BioHistoryEvent) {
        when(event) {
            is BioHistoryEvent.Delete -> {
                viewModelScope.launch {
                    when (val result = deleteMeasurementUseCase(event.measurement)) {
                        is DomainResult.Success -> (Unit)
                        is DomainResult.Error -> {
                            _state.value = _state.value.copy(
                                error = (result.error.toUiError().message)
                            )
                        }
                    }
                }
            }
            is BioHistoryEvent.NavigateClick -> {
                viewModelScope.launch {
                    _navigateEvent.send(event.stress)
                }
            }
        }
    }
}