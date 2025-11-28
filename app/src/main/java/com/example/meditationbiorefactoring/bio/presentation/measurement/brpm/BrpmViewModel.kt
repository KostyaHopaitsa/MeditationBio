package com.example.meditationbiorefactoring.bio.presentation.measurement.brpm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.bio.domain.model.MeasurementResult
import com.example.meditationbiorefactoring.bio.domain.use_case.ComputeBrpmUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.ResetBrpmMeasurementUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.CollectZValuesUseCase
import com.example.meditationbiorefactoring.bio.domain.util.BioParamType
import com.example.meditationbiorefactoring.bio.presentation.measurement.MeasurementAggregator
import com.example.meditationbiorefactoring.bio.presentation.measurement.util.ErrorType
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
    private val computeBrpmUseCase: ComputeBrpmUseCase,
    private val collectZValuesUseCase: CollectZValuesUseCase,
    private val resetBrpmMeasurementUseCase: ResetBrpmMeasurementUseCase,
    private val aggregator: MeasurementAggregator
): ViewModel() {

    private val _state = MutableStateFlow(BrpmState())
    val state: StateFlow<BrpmState> = _state

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private val _navigateEvent = Channel<Unit>(Channel.BUFFERED)
    val navigateEvent = _navigateEvent.receiveAsFlow()

    fun onEvent(event: BrpmEvent) {
        when(event) {
            is BrpmEvent.Start -> {
                _state.value = _state.value.copy(
                    isMeasuring = true,
                    isLoading = false,
                )
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
                _progress.value = 0f
                _state.value = BrpmState()
                viewModelScope.launch {
                    resetBrpmMeasurementUseCase()
                }
            }
        }
    }

    private fun processFrame(z: Double) {
        viewModelScope.launch {
            val zValuesCollector = collectZValuesUseCase(z)
            val brpm = computeBrpmUseCase(zValuesCollector.values,zValuesCollector.progress)
            _progress.value = brpm.progress

            when (val result = brpm.result) {
                is MeasurementResult.Success -> {
                    _state.value = _state.value.copy(
                        isMeasuring = false,
                        isMeasured = true,
                        value = String.format(Locale.US, "%.2f", result.value),
                        status = if (result.value < 12) "low"
                        else if (result.value > 25) "high"
                        else "normal",
                    )
                    aggregator.updateMeasurement(BioParamType.brpm, result.value)
                }
                is MeasurementResult.Invalid -> {
                    _state.value = _state.value.copy(
                        isMeasuring = false,
                        error = ErrorType.MeasureError,
                    )
                }
                is MeasurementResult.Error -> {
                    _state.value = _state.value.copy(
                        error = ErrorType.UnknownError,
                    )
                }
            }
        }
    }
}