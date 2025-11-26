package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.bpm

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementResult
import com.example.meditationbiorefactoring.feature_bio.domain.use_case.BpmMeasurementUseCase
import com.example.meditationbiorefactoring.feature_bio.domain.use_case.ResetBpmMeasurementUseCase
import com.example.meditationbiorefactoring.feature_bio.domain.util.BioParamType
import com.example.meditationbiorefactoring.feature_bio.presentation.measurement.MeasurementAggregator
import com.example.meditationbiorefactoring.feature_bio.presentation.measurement.util.ErrorType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BpmViewModel @Inject constructor(
    private val bpmMeasurementUseCase: BpmMeasurementUseCase,
    private val resetBpmMeasurementUseCase: ResetBpmMeasurementUseCase,
    private val aggregator: MeasurementAggregator
) : ViewModel() {

    private val _state = MutableStateFlow(BpmState())
    val state: StateFlow<BpmState> = _state

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress

    private val _navigateEvent = Channel<Unit>(Channel.BUFFERED)
    val navigateEvent = _navigateEvent.receiveAsFlow()

    init {
        Log.d("BpmState", "${state.value}")
        Log.d("BpmState", "${aggregator.state.value}")
    }

    fun onEvent(event: BpmEvent) {
        when (event) {
            is BpmEvent.Start -> {
                _state.value = _state.value.copy(
                    isMeasuring = true,
                    isLoading = false,
                    isTorchEnabled = true,
                )
            }
            is BpmEvent.FrameCaptured -> {
                processFrame(event.buffer)
            }
            is BpmEvent.NavigateClick -> {
                viewModelScope.launch {
                    _navigateEvent.send(Unit)
                }
            }
            is BpmEvent.Reset -> {
                _progress.value = 0f
                _state.value = BpmState()
                viewModelScope.launch {
                    resetBpmMeasurementUseCase()
                }
            }
        }
    }

    fun processFrame(buffer: ByteArray) {
        viewModelScope.launch {
            val analysis = bpmMeasurementUseCase(buffer)
            _progress.value = analysis.progress

            when (val result = analysis.result) {
                is MeasurementResult.Success -> {
                    _state.value = _state.value.copy(
                        isMeasuring = false,
                        isMeasured = true,
                        value = result.value.toString(),
                        status = if (result.value < 60) "low"
                        else if (result.value > 100) "high"
                        else "normal",
                    )
                    aggregator.updateMeasurement(BioParamType.bpm, result.value)
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