package com.example.meditationbiorefactoring.bio.presentation.measurement.bpm

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.meditationbiorefactoring.bio.domain.use_case.core_use_case.PpgCoreUseCases
import com.example.meditationbiorefactoring.bio.domain.model.BioParamType
import com.example.meditationbiorefactoring.bio.domain.model.PpgData
import com.example.meditationbiorefactoring.bio.presentation.measurement.MeasurementAggregator
import com.example.meditationbiorefactoring.common.DomainResult
import com.example.meditationbiorefactoring.common.toUiError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale

@HiltViewModel
class BpmViewModel @Inject constructor(
    private val ppgCoreUseCases: PpgCoreUseCases,
    private val aggregator: MeasurementAggregator,
) : ViewModel() {

    private val _state = MutableStateFlow(BpmState())
    val state: StateFlow<BpmState> = _state
    private val _navigateEvent = Channel<Unit>(Channel.BUFFERED)
    val navigateEvent = _navigateEvent.receiveAsFlow()

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
                _state.value = BpmState()
                viewModelScope.launch {
                    ppgCoreUseCases.resetBpmMeasurementUseCase()
                }
            }
        }
    }

    private fun processFrame(buffer: ByteArray) {
        val ppgCollector = ppgCoreUseCases.collectPpgSignalUseCase(buffer)

        _state.value = _state.value.copy(
            progress = ppgCollector.progress
        )

        if(ppgCollector.progress >= 1f) computeResult(ppgCollector)
    }

    private fun computeResult(ppgData: PpgData) {
        val result = ppgCoreUseCases.computeBpmUseCase(
            ppgData.values,
            ppgData.timestamps,
        )

        when (result) {
            is DomainResult.Success -> {
                _state.value = _state.value.copy(
                    isMeasuring = false,
                    isMeasured = true,
                    value = String.format(Locale.US, "%.2f", result.data),
                    status = if (result.data < 60) "low"
                    else if (result.data > 100) "high"
                    else "normal",
                )
                aggregator.updateMeasurement(BioParamType.BPM, result.data)
            }

            is DomainResult.Error -> {
                _state.value = _state.value.copy(
                    error = result.error.toUiError().message,
                )
            }
        }
    }

}