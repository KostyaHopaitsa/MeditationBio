package com.example.meditationbiorefactoring.music.data.remote.dto

import com.example.meditationbiorefactoring.music.domain.model.Track
import com.google.gson.annotations.SerializedName

data class TrackResponseDto(
    val results: List<TrackDto>
)

data class TrackDto(
    val id: String,
    val name: String,
    @SerializedName("artist_name")
    val artistName: String,
    @SerializedName("album_name")
    val albumName: String?,
    @SerializedName("album_image")
    val image: String?,
    val audio: String
)

fun TrackDto.toTrack(): Track {
    return Track(
        id = id,
        title = name,
        artist = artistName,
        album = albumName,
        imageUrl = image,
        audioUrl = audio,
    )
}