package com.example.meditationbiorefactoring.bio.presentation.measurement.brpm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meditationbiorefactoring.common.components.Error
import com.example.meditationbiorefactoring.bio.presentation.measurement.components.MeasurementStart
import com.example.meditationbiorefactoring.bio.presentation.measurement.components.MeasurementResult

@Composable
fun BrpmScreen(
    onNavigateToSiv: () -> Unit,
    viewModel: BrpmViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateEvent.collect {
            onNavigateToSiv()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.isMeasuring -> {
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(25.dp),
                )
            }
            state.isMeasured -> {
                MeasurementResult(
                    status = state.status,
                    value = state.value,
                    type = "BRPM",
                    buttonDescription = "To SIV",
                    onNavigate =  { viewModel.onEvent(BrpmEvent.NavigateClick) },
                    onRestart = { viewModel.onEvent(BrpmEvent.Reset) }
                )
            }
            state.error.isNotBlank() -> {
                Error(
                    message = state.error,
                    onRetry = { viewModel.onEvent(BrpmEvent.Reset) }
                )
            }
            else -> {
                MeasurementStart(
                    type = "BRPM",
                    onStart = {
                        viewModel.onEvent(BrpmEvent.Reset)
                        viewModel.onEvent(BrpmEvent.Start)
                    }
                )
            }
        }
    }
}