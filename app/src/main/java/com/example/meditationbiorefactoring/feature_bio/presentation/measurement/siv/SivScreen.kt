package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.siv

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meditationbiorefactoring.feature_bio.presentation.util.ErrorType
import com.example.meditationbiorefactoring.common.presentation.components.Error
import com.example.meditationbiorefactoring.feature_bio.presentation.measurement.components.MeasurementStart
import com.example.meditationbiorefactoring.feature_bio.presentation.measurement.components.MeasurementResult

@Composable
fun SivScreen(
    onNavigateToMusic: (String) -> Unit,
    viewModel: SivViewModel = hiltViewModel(),
) {
    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.navigateEvent.collect { overall ->
            onNavigateToMusic(overall)
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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Recording... Speak now", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(onClick = { viewModel.onEvent(SivEvent.Stop) }) {
                        Text("Stop")
                    }
                }
            }
            state.isMeasured -> {
                MeasurementResult(
                    status = state.status,
                    value = state.value,
                    type = "SIV",
                    buttonDescription = "To Music",
                    onNavigate = { viewModel.onEvent(SivEvent.NavigateClick) },
                    onRestart = { viewModel.onEvent(SivEvent.Reset) }
                )
            }
            state.error != null -> {
                val errorMessage = when (state.error) {
                    ErrorType.SensorError -> "Micro initialization failed"
                    ErrorType.MeasureError -> "Measurement failed"
                    ErrorType.UnknownError -> "Unknown error"
                }
                Error(
                    message = errorMessage,
                    onRetry = { viewModel.onEvent(SivEvent.Reset) }
                )
            }
            else -> {
                MeasurementStart(
                    type = "SIV",
                    onStart = { viewModel.onEvent(SivEvent.Start) }
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onEvent(SivEvent.Reset)
        }
    }
}