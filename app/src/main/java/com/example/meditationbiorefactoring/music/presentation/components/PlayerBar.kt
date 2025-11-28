package com.example.meditationbiorefactoring.music.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.meditationbiorefactoring.music.domain.model.Track

@Composable
fun PlayerBar(
    track: Track,
    progress: Float,
    buttonIcon: ImageVector,
    onPlayControl: () -> Unit,
    onSeek: (Float) -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.LightGray,
                )
                .pointerInput(Unit) {},
            verticalArrangement = Arrangement.Bottom,
        ) {
            MusicItem(
                track = track,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                IconButton(onClick = onPlayControl) {
                    Icon(
                        imageVector = buttonIcon,
                        contentDescription = "listen",
                        modifier = Modifier.size(30.dp)
                    )
                }
                Slider(
                    value = progress,
                    onValueChange = { newValue -> onSeek(newValue) },
                    valueRange = 0f..1f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}