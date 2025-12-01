package com.example.meditationbiorefactoring.bio.presentation.measurement.siv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.bio.domain.model.MeasurementResult
import com.example.meditationbiorefactoring.bio.domain.sensors.AudioRecorder
import com.example.meditationbiorefactoring.bio.domain.use_case.AudioCoreUseCases
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
class SivViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val audioCoreUseCases: AudioCoreUseCases,
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
                    audioRecorder.start { chunk ->
                        audioCoreUseCases.addChunkUseCase(chunk)
                    }
                    _state.value = _state.value.copy(isMeasuring = true)
                }
            }
            is SivEvent.Stop -> {
                viewModelScope.launch {
                    audioRecorder.stop()
                }
                val buffer = audioCoreUseCases.buildAudioBufferUseCase()
                val analysis = audioCoreUseCases.computeSivUseCase(buffer, buffer.size)
                when (val result = analysis.result) {
                    is MeasurementResult.Success -> {
                        _state.value = _state.value.copy(
                            isMeasuring = false,
                            isMeasured = true,
                            value = String.format(Locale.US, "%.3f", result.value),
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
            SivEvent.Reset -> {
                _state.value = SivState()
                viewModelScope.launch {
                    audioCoreUseCases.resetSivMeasurementUseCase()
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