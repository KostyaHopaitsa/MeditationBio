package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.bpm.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.meditationbiorefactoring.feature_bio.presentation.measurement.bpm.CameraController

@Composable
fun CameraPreview(
    cameraController: CameraController,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { cameraController.previewView }
    )
}