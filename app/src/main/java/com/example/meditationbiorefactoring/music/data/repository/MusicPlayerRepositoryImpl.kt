package com.example.meditationbiorefactoring.music.data.repository

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.meditationbiorefactoring.music.domain.repository.MusicPlayerRepository
import javax.inject.Inject

class MusicPlayerRepositoryImpl @Inject constructor(
    private val player: ExoPlayer
) : MusicPlayerRepository {

    override fun play(url: String) {
        val exoPlayer = player
        val currentUrl = exoPlayer.currentMediaItem?.localConfiguration?.uri?.toString()

        if (currentUrl != url) {
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }

        exoPlayer.playWhenReady = true
    }

    override fun pause() {
        player.pause()
    }

    override fun resume() {
        player.playWhenReady = true
    }

    override fun stop() {
        player.stop()
    }

    override fun release() {
        player.release()
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    override fun getCurrentPosition(): Long = player.currentPosition

    override fun getDuration(): Long = player.duration

    override fun isPlaying(): Boolean {
        return player.playWhenReady && player.playbackState == Player.STATE_READY
    }
}