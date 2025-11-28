package com.example.meditationbiorefactoring.bio.presentation.measurement.bpm


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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.meditationbiorefactoring.bio.presentation.measurement.util.ErrorType
import com.example.meditationbiorefactoring.common.presentation.components.Error
import com.example.meditationbiorefactoring.bio.presentation.measurement.components.MeasurementStart
import com.example.meditationbiorefactoring.bio.presentation.measurement.components.MeasurementResult
import com.example.meditationbiorefactoring.bio.presentation.measurement.bpm.components.CameraPreview

@Composable
fun BpmScreen(
    onNavigateToBrpm: () -> Unit,
    viewModel: BpmViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val cameraController = remember {
        CameraController(context, lifecycleOwner) { buffer ->
            viewModel.processFrame(buffer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigateEvent.collect {
            onNavigateToBrpm()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.isMeasuring -> {
                CameraPreview(
                    cameraController,
                    modifier = Modifier.fillMaxSize()
                )
                LinearProgressIndicator(
                    progress = { progress },
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
                    type = "BPM",
                    buttonDescription = "To BRPM",
                    onNavigate = { viewModel.onEvent(BpmEvent.NavigateClick) },
                    onRestart = { viewModel.onEvent(BpmEvent.Reset) }
                )

            }
            state.error != null -> {
                val errorMessage = when (state.error!!) {
                    ErrorType.SensorError -> "Camera initialization failed"
                    ErrorType.MeasureError -> "Measurement failed"
                    ErrorType.UnknownError -> "Unknown error"
                }
                Error(
                    message = errorMessage,
                    onRetry = { viewModel.onEvent(BpmEvent.Reset) }
                )
            }
            else -> {
                MeasurementStart(
                    type = "BPM",
                    onStart = {
                        viewModel.onEvent(BpmEvent.Reset)
                        viewModel.onEvent(BpmEvent.Start)
                    }
                )
            }
        }
    }
}