package com.example.meditationbiorefactoring.bio.presentation.bio_history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.meditationbiorefactoring.bio.presentation.bio_history.components.MeasureItem

@Composable
fun BioHistoryScreen(
    onNavigateToMusic: (String) -> Unit,
    viewModel: BioHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateEvent.collect { stress ->
            onNavigateToMusic(stress)
        }
    }

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
                        onNavigateTo = { viewModel.onEvent(
                            BioHistoryEvent.NavigateClick(measurement.stress)
                        ) },
                        onDelete = { viewModel.onEvent(
                            BioHistoryEvent.Delete(measurement)
                        ) },
                        measurement = measurement
                    )
                }
            }
        }
    }
}