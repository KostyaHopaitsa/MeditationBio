package com.example.meditationbiorefactoring.feature_bio.presentation.measurement.bpm

import android.content.Context
import android.graphics.ImageFormat
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.meditationbiorefactoring.feature_bio.presentation.measurement.util.getCameraProvider

class CameraController(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val onFrameCaptured: (ByteArray) -> Unit
) {
    val previewView: PreviewView = PreviewView(context)
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    @OptIn(ExperimentalGetImage::class)
    suspend fun startCamera() {
        cameraProvider = context.getCameraProvider()

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

                onFrameCaptured(data)
            }
            imageProxy.close()
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider?.unbindAll()
            camera = cameraProvider?.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun enableTorch(enable: Boolean) {
        camera?.cameraControl?.enableTorch(enable)
    }

    fun stopCamera() {
        camera?.cameraControl?.enableTorch(false)
        cameraProvider?.unbindAll()
    }
}