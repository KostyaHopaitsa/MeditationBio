package com.example.meditationbiorefactoring.music.data.repository

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.meditationbiorefactoring.music.domain.repository.MusicPlayerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MusicPlayerRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MusicPlayerRepository {

    private var player: ExoPlayer? = null

    private fun getOrCreatePlayer(): ExoPlayer {
        if (player == null) {
            player = ExoPlayer.Builder(context).build()
        }
        return player!!
    }

    override fun play(url: String) {
        val exoPlayer = getOrCreatePlayer()
        val currentUrl = exoPlayer.currentMediaItem?.localConfiguration?.uri?.toString()

        if (currentUrl != url) {
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }

        exoPlayer.playWhenReady = true
    }

    override fun pause() {
        player?.pause()
    }

    override fun resume() {
        player?.playWhenReady = true
    }

    override fun stop() {
        player?.stop()
    }

    override fun release() {
        player?.release()
        player = null
    }

    override fun seekTo(positionMs: Long) {
        player?.seekTo(positionMs)
    }

    override fun getCurrentPosition(): Long = player?.currentPosition ?: 0L

    override fun getDuration(): Long = player?.duration ?: 0L

    override fun isPlaying(): Boolean {
        val p = player ?: return false
        return p.playWhenReady && p.playbackState == Player.STATE_READY
    }
}