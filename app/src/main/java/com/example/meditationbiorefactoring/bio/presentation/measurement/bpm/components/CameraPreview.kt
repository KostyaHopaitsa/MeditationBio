package com.example.meditationbiorefactoring.bio.presentation.measurement.bpm.components

import android.graphics.ImageFormat
import android.util.Size
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.meditationbiorefactoring.bio.presentation.measurement.util.getCameraProvider

@ExperimentalGetImage
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onFrame: (ByteArray) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    var camera: Camera? = null
    var cameraProvider: ProcessCameraProvider? = null


    AndroidView(
        factory = { previewView },
        modifier = modifier
    )

    LaunchedEffect(Unit) {
        cameraProvider = context.getCameraProvider()
        val preview = Preview.Builder()
            .build()
            .apply { setSurfaceProvider(previewView.surfaceProvider) }

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
                onFrame(data)
            }
            imageProxy.close()
        }

        cameraProvider.unbindAll()
        camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageAnalysis
        )

        camera.cameraControl.enableTorch(true)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                camera?.cameraControl?.enableTorch(true)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            camera?.cameraControl?.enableTorch(false)
            cameraProvider?.unbindAll()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}