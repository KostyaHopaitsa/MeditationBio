package com.example.meditationbiorefactoring.bio.presentation.measurement

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import com.example.meditationbiorefactoring.bio.domain.use_case.ComputeOverallStressUseCase
import com.example.meditationbiorefactoring.bio.domain.use_case.InsertMeasurementUseCase
import com.example.meditationbiorefactoring.bio.domain.util.BioParamType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeasurementAggregator @Inject constructor(
    private val computeOverallStressUseCase: ComputeOverallStressUseCase,
    private val insertMeasurementUseCase: InsertMeasurementUseCase
) {
    private val _state = mutableStateOf(MeasurementState())
    val state: State<MeasurementState> = _state

    fun updateMeasurement(type: BioParamType, value: Double) {
        _state.value = when (type) {
            BioParamType.bpm -> _state.value.copy(stressData = _state.value.stressData.copy(bpm = value))
            BioParamType.brpm -> _state.value.copy(stressData = _state.value.stressData.copy(brpm = value))
            BioParamType.siv -> _state.value.copy(stressData = _state.value.stressData.copy(siv = value))
        }
    }

    fun computeOverallStress() {
        _state.value = _state.value.copy(
            overallStress = computeOverallStressUseCase(_state.value.stressData)
        )
    }

    suspend fun saveMeasurement() {
        val data = _state.value.stressData
        if (data.isComplete()) {
            insertMeasurementUseCase(
                Measurement(
                    timestamp = System.currentTimeMillis(),
                    bpm = data.bpm!!,
                    brpm = data.brpm!!,
                    siv = data.siv!!,
                    stress = _state.value.overallStress
                )
            )
        }
    }
}