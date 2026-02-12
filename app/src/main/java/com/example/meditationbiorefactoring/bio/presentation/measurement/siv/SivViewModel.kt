package com.example.meditationbiorefactoring.bio.presentation.measurement.siv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.AudioCoreUseCases
import com.example.meditationbiorefactoring.bio.domain.use_case.sensor_use_case.StartAudioUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.sensor_use_case.StopAudioUseCase
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
class SivViewModel @Inject constructor(
    private val startAudioUseCase: StartAudioUseCase,
    private val stopAudioUseCase: StopAudioUseCase,
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
                    startAudioUseCase().collect { result ->
                        when(result) {
                            is DomainResult.Success -> {
                                audioCoreUseCases.addChunkUseCase(result.data)
                            }
                            is DomainResult.Error -> {
                                _state.value = _state.value.copy(
                                    error = result.error.toUiError().message,
                                    isMeasuring = false
                                )
                            }
                        }
                    }
                }
                _state.value = _state.value.copy(isMeasuring = true)
            }
            is SivEvent.Stop -> {
                stopAudioUseCase()
                val buffer = audioCoreUseCases.buildAudioBufferUseCase()
                when (val result = audioCoreUseCases.computeSivUseCase(buffer)) {
                    is DomainResult.Success -> {
                        _state.value = _state.value.copy(
                            isMeasuring = false,
                            isMeasured = true,
                            value = String.format(Locale.US, "%.3f", result.data),
                            status = if (result.data < 0.03) "low"
                            else if (result.data > 0.09) "high"
                            else "normal",
                        )
                        aggregator.updateMeasurement(BioParamType.SIV, result.data)
                        aggregator.computeOverallStress()
                    }
                    is DomainResult.Error -> {
                        _state.value = _state.value.copy(
                            isMeasuring = false,
                            error = result.error.toUiError().message
                        )
                    }
                }
            }
            is SivEvent.Reset -> {
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