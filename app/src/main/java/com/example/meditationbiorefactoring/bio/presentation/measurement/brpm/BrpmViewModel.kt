package com.example.meditationbiorefactoring.bio.presentation.measurement.brpm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.bio.domain.model.ZData
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.BreathCoreUseCases
import com.example.meditationbiorefactoring.bio.domain.use_case.sensor_use_case.StartAccelerometerUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.sensor_use_case.StopAccelerometerUseCase
import com.example.meditationbiorefactoring.bio.domain.model.BioParamType
import com.example.meditationbiorefactoring.bio.presentation.measurement.MeasurementAggregator
import com.example.meditationbiorefactoring.common.DomainResult
import com.example.meditationbiorefactoring.common.toUiError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BrpmViewModel @Inject constructor(
    private val breathCoreUseCases: BreathCoreUseCases,
    private val startAccelerometerUseCase: StartAccelerometerUseCase,
    private val stopAccelerometerUseCase: StopAccelerometerUseCase,
    private val aggregator: MeasurementAggregator
): ViewModel() {

    private val _state = MutableStateFlow(BrpmState())
    val state: StateFlow<BrpmState> = _state
    private val _navigateEvent = Channel<Unit>(Channel.BUFFERED)
    val navigateEvent = _navigateEvent.receiveAsFlow()

    fun onEvent(event: BrpmEvent) {
        when(event) {
            is BrpmEvent.Start -> {
                _state.value = _state.value.copy(
                    isMeasuring = true,
                    isLoading = false,
                )
                viewModelScope.launch {
                    startAccelerometerUseCase().collect { values ->
                        val zSignalResult = breathCoreUseCases
                            .collectZValuesUseCase(values[2].toDouble())
                        onEvent(BrpmEvent.DataCaptured(zSignalResult))
                    }
                }
            }
            is BrpmEvent.DataCaptured -> {
                processFrame(event.z)
            }
            is BrpmEvent.NavigateClick -> {
                viewModelScope.launch {
                    _navigateEvent.send(Unit)
                }
            }
            BrpmEvent.Reset -> {
                _state.value = BrpmState()
                viewModelScope.launch {
                    breathCoreUseCases.resetBrpmMeasurementUseCase()
                }
            }
        }
    }

    private fun processFrame(z: ZData) {
        if (z.progress >= 1f) stopAccelerometerUseCase()
        val result = breathCoreUseCases.computeBrpmUseCase(z.values, z.progress)
        _state.value = _state.value.copy(
            progress = z.progress
        )

        when (result) {
            is DomainResult.Success -> {
                _state.value = _state.value.copy(
                    isMeasuring = false,
                    isMeasured = true,
                    value = String.format(Locale.US, "%.2f", result.data),
                    status = if (result.data < 12) "low"
                    else if (result.data > 25) "high"
                    else "normal",
                )
                aggregator.updateMeasurement(BioParamType.BRPM, result.data)
            }

            is DomainResult.Error -> {
                _state.value = _state.value.copy(
                    error = result.error.toUiError().message,
                )
            }
        }
    }
}