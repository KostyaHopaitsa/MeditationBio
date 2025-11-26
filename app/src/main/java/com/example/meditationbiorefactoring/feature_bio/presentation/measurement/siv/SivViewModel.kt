package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.siv

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementResult
import com.example.meditationbiorefactoring.feature_bio.domain.use_case.ComputeSivUseCase
import com.example.meditationbiorefactoring.feature_bio.domain.use_case.GetRawSivDataUseCase
import com.example.meditationbiorefactoring.feature_bio.domain.use_case.ResetSivMeasurementUseCase
import com.example.meditationbiorefactoring.feature_bio.domain.use_case.StartSivRecordingUseCase
import com.example.meditationbiorefactoring.feature_bio.domain.use_case.StopSivUseCase
import com.example.meditationbiorefactoring.feature_bio.domain.util.BioParamType
import com.example.meditationbiorefactoring.feature_bio.presentation.measurement.MeasurementAggregator
import com.example.meditationbiorefactoring.feature_bio.presentation.measurement.util.ErrorType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SivViewModel @Inject constructor(
    private val startSivRecordingUseCase: StartSivRecordingUseCase,
    private val stopSivUseCase: StopSivUseCase,
    private val getRawSivDataUseCase: GetRawSivDataUseCase,
    private val computeSivUseCase: ComputeSivUseCase,
    private val resetSivMeasurementUseCase: ResetSivMeasurementUseCase,
    private val aggregator: MeasurementAggregator,
): ViewModel() {

    private val _state = MutableStateFlow(SivState())
    val state: StateFlow<SivState> = _state

    private val _navigateEvent = Channel<String>(Channel.BUFFERED)
    val navigateEvent = _navigateEvent.receiveAsFlow()

    fun onEvent(event: SivEvent) {
        when (event) {
            is SivEvent.Start -> {
                viewModelScope.launch {
                    startSivRecordingUseCase()
                    _state.value = _state.value.copy(isMeasuring = true)
                }
            }
            is SivEvent.Stop -> {
                viewModelScope.launch {
                    stopSivUseCase()
                    val (buffer, length) = getRawSivDataUseCase()
                    Log.d("analysis", "$buffer, $length")
                    val analysis = computeSivUseCase(buffer, length)
                    Log.d("analysis", "$analysis")
                    when (val result = analysis.result) {
                        is MeasurementResult.Success -> {
                            _state.value = _state.value.copy(
                                isMeasuring = false,
                                isMeasured = true,
                                value = result.value.toString(),
                                status = if (result.value < 0.03) "low"
                                else if (result.value > 0.09) "high"
                                else "normal",
                            )
                            aggregator.updateMeasurement(BioParamType.siv, result.value)
                            aggregator.computeOverallStress()
                        }
                        is MeasurementResult.Invalid -> {
                            _state.value = _state.value.copy(
                                isMeasuring = false,
                                error = ErrorType.MeasureError
                            )
                        }
                        is MeasurementResult.Error -> {
                            _state.value = _state.value.copy(
                                error = ErrorType.UnknownError
                            )
                        }
                    }
                }
            }
            SivEvent.Reset -> {
                _state.value = SivState()
                viewModelScope.launch {
                    resetSivMeasurementUseCase()
                }
            }
            is SivEvent.NavigateClick -> {
                viewModelScope.launch {
                    aggregator.saveMeasurement()
                    _navigateEvent.send(aggregator.state.value.overallStress)
                }
            }
        }
    }
}