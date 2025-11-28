package com.example.meditationbiorefactoring.bio.presentation.bio_history.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meditationbiorefactoring.bio.domain.model.Measurement
import com.example.meditationbiorefactoring.bio.presentation.util.toReadableDate


@Composable
fun MeasureItem(
    onNavigateTo: () -> Unit,
    measurement: Measurement
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(10.dp),
            )
            .background(color = Color.LightGray)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = measurement.timestamp.toReadableDate(), fontSize = 15.sp)
            Text(text = "BPM: ${measurement.bpm}", fontSize = 15.sp)
            Text(text = "BRPM: ${measurement.brpm}", fontSize = 15.sp)
            Text(text = "SIV: ${measurement.siv}", fontSize = 15.sp)
            Text(text = "Stress: ${measurement.stress}", fontSize = 15.sp)
        }
        IconButton(onClick = onNavigateTo) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "listen",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}