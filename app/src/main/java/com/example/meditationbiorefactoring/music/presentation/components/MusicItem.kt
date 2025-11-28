package com.example.meditationbiorefactoring.music.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.meditationbiorefactoring.music.domain.model.Track

@Composable
fun MusicItem(
    track: Track,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RectangleShape
                )
                .size(100.dp),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = track.imageUrl ?: Icons.Default.Warning,
                contentDescription = "track image",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(text = track.title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text(text = track.artist, fontSize = 15.sp)
        }
    }
}