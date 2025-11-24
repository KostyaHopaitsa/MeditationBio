package com.example.meditationbiorefactoring.feature_bio.presentation.bio_history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meditationbiorefactoring.feature_bio.presentation.bio_history.components.MeasureItem

@Composable
fun BioHistoryScreen(
    onNavigateToMusic: (Int) -> Unit,
    viewModel: BioHistoryViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.error}")
            }
        }
        state.measurements.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No measurements yet")
            }
        }
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.measurements) { measurement ->
                    MeasureItem(
                        onNavigateTo = { measurement.id?.let { onNavigateToMusic(it) } },
                        measurement = measurement
                    )
                }
            }
        }
    }
}