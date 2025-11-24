package com.example.meditationbiorefactoring.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.meditationbiorefactoring.app.navigation.AppNavHost
import com.example.meditationbiorefactoring.ui.theme.MeditationBioRefactoringTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
            val micGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: false

            when{
                !cameraGranted -> {
                    Toast.makeText(
                        this,
                        "Camera permissions is required",
                        Toast.LENGTH_LONG
                    ).show()
                }
                !micGranted -> {
                    Toast.makeText(
                        this,
                        "microphone permissions is required",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> { launchApp() }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val neededPermissions = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            neededPermissions.add(Manifest.permission.CAMERA)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            neededPermissions.add(Manifest.permission.RECORD_AUDIO)
        }

        if (neededPermissions.isEmpty()) {
            launchApp()
        } else {
            requestPermissionsLauncher.launch(neededPermissions.toTypedArray())
        }
    }

    private fun launchApp() {
        setContent {
            MeditationBioRefactoringTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavHost()
                }
            }
        }
    }
}

