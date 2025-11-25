package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.bpm.components

import android.graphics.ImageFormat
import android.util.Log
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.meditationbiorefactoring.feature_bio.data.analyzer.PpgAnalyzerCore
import com.example.meditationbiorefactoring.feature_bio.util.getCameraProvider

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onFrameCaptured: (ByteArray) -> Unit,
    enableTorch: Boolean = true
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val testAnalyzer = remember { PpgAnalyzerCore() }
    var camera by remember { mutableStateOf<Camera?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { previewView }
    )

    LaunchedEffect(Unit) {
        val cameraProvider = context.getCameraProvider()

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(640, 480))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
            val img = imageProxy.image
            if (img != null && img.format == ImageFormat.YUV_420_888) {
                val buffer = img.planes[0].buffer
                buffer.rewind()
                val data = ByteArray(buffer.remaining())
                buffer.get(data)

                val bpmResult = testAnalyzer.analyzeFrame(data)
                Log.d("CameraPreview", "Frame captured: size=${data.size}")
                Log.d("CameraPreview", "BpmResult=$bpmResult")

                onFrameCaptured(data)
            }
            imageProxy.close()
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("CameraPreview", "Camera bind failed", e)
        }
    }

    LaunchedEffect(enableTorch, camera) {
        camera?.cameraControl?.enableTorch(enableTorch)
    }

    DisposableEffect(lifecycleOwner, camera) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && enableTorch) {
                camera?.cameraControl?.enableTorch(true)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}