package com.example.meditationbiorefactoring.feature_bio.domain.use_case

import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementAnalysis
import com.example.meditationbiorefactoring.feature_bio.domain.model.MeasurementResult
import com.example.meditationbiorefactoring.feature_bio.domain.repository.SivRepository
import javax.inject.Inject

class ComputeSivUseCase @Inject constructor(
    private val repository: SivRepository
) {
    suspend operator fun invoke(
        buffer: ShortArray,
        length: Int
    ): MeasurementAnalysis {
        return if (length > 0 || buffer.isNotEmpty()) {
            val siv = repository.computeSiv(buffer, length)
            if (siv in 0.001..0.15) {
                MeasurementAnalysis(MeasurementResult.Success(siv))
            } else {
                MeasurementAnalysis(MeasurementResult.Invalid(siv))
            }
        } else {
            MeasurementAnalysis(MeasurementResult.Error)
        }
    }
}