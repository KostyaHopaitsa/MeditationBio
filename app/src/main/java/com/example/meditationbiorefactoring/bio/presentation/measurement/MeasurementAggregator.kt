package com.example.meditationbiorefactoring.bio.presentation.measurement

import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import com.example.meditationbiorefactoring.bio.domain.use_case.ComputeOverallStressUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.measurement_use_case.InsertMeasurementUseCase
import com.example.meditationbiorefactoring.bio.domain.model.BioParamType
import com.example.meditationbiorefactoring.bio.presentation.measurement.util.MeasurementState
import com.example.meditationbiorefactoring.common.DomainError
import com.example.meditationbiorefactoring.common.DomainResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeasurementAggregator @Inject constructor(
    private val computeOverallStressUseCase: ComputeOverallStressUseCase,
    private val insertMeasurementUseCase: InsertMeasurementUseCase
) {
    private val _state = MutableStateFlow(MeasurementState())
    val state: StateFlow<MeasurementState> = _state.asStateFlow()

    fun updateMeasurement(type: BioParamType, value: Double) {
        _state.update { current ->
            when (type) {
                BioParamType.BPM -> current.copy(stressData = current.stressData.copy(bpm = value))
                BioParamType.BRPM -> current.copy(stressData = current.stressData.copy(brpm = value))
                BioParamType.SIV -> current.copy(stressData = current.stressData.copy(siv = value))
            }
        }
    }

    fun computeOverallStress(): DomainResult<Unit> {
        return when(val result = computeOverallStressUseCase(_state.value.stressData)) {
            is DomainResult.Success -> {
                val stressLevel = when {
                    result.data < 3 -> "Low"
                    result.data < 5 -> "Middle"
                    else -> "High"
                }
                _state.update { it.copy(overallStress = stressLevel) }
                DomainResult.Success(Unit)
            }
            is DomainResult.Error -> {
                DomainResult.Error(result.error)
            }
        }
    }

    suspend fun saveMeasurement(): DomainResult<Unit> {
        val data = _state.value.stressData
        return if (data.bpm != null && data.brpm != null && data.siv != null) {
            insertMeasurementUseCase(
                Measurement(
                    timestamp = System.currentTimeMillis(),
                    bpm = data.bpm,
                    brpm = data.brpm,
                    siv = data.siv,
                    stress = _state.value.overallStress
                )
            )
        } else {
            DomainResult.Error(DomainError.NotComplete)
        }
    }
}