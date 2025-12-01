import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.example.meditationbiorefactoring.bio.domain.sensors.AudioRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AudioRecorderController : AudioRecorder {

    private val sampleRate = 16000
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT

    private var recorder: AudioRecord? = null
    private var bufferSize = 0
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun start(onChunk: (ShortArray) -> Unit) {
        try {
            bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
            if (bufferSize <= 0) return

            recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
            )

            if (recorder?.state != AudioRecord.STATE_INITIALIZED) {
                return
            }

            val buffer = ShortArray(bufferSize)
            recorder?.startRecording()

            job = scope.launch {
                while (isActive) {
                    val read = recorder?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        onChunk(buffer.copyOfRange(0, read))
                    }
                }
            }

        } catch (e: SecurityException) {
            return
        }
    }

    override fun stop() {
        job?.cancel()
        recorder?.stop()
        recorder?.release()
        recorder = null
    }
}