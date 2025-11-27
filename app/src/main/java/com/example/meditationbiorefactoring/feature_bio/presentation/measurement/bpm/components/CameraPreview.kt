package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.bpm.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.meditationbiorefactoring.feature_bio.presentation.measurement.bpm.CameraController

@Composable
fun CameraPreview(
    cameraController: CameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = { cameraController.previewView }
    )

    LaunchedEffect(Unit) {
        cameraController.startCamera()
        cameraController.enableTorch(true)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                cameraController.enableTorch(true)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            cameraController.enableTorch(false)
            cameraController.stopCamera()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}