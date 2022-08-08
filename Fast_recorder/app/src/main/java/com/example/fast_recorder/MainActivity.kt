package com.example.fast_recorder

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    private val requiredPermissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    private val soundVisualizerView: SoundVisualizerView by lazy {
        findViewById<SoundVisualizerView>(R.id.soundVisualizerView)
    }

    private val recordTimeTextView: CountUpTextView by lazy {
        findViewById<CountUpTextView>(R.id.recordTimeTextView)
    }

    private val recordButton: RecordButton by lazy {
        findViewById<RecordButton>(R.id.recordButton)
    }

    private val resetButton: Button by lazy {
        findViewById<Button>(R.id.resetButton)
    }

    private var state = State.BEFORE_RECORDING
    set(value) {
        field = value
        recordButton.updateIconWithState(value)
        resetButton.isEnabled = (value == State.AFTER_RECORDING) || (value == State.ON_PLAYING)
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private val recordingFilePath: String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission()
        state = State.BEFORE_RECORDING

        recordButton.updateIconWithState(state)
        recordButton.setOnClickListener {
            when(state) {
                State.BEFORE_RECORDING -> startRecording()
                State.ON_RECORDING -> stopRecording()
                State.AFTER_RECORDING -> startPlaying()
                State.ON_PLAYING -> stopPlaying()
            }
        }
        resetButton.setOnClickListener {
            stopPlaying()
            state = State.BEFORE_RECORDING
            soundVisualizerView.resetVisualizing()
        }

        soundVisualizerView.onRequestCurrentAmplitude = {
            recorder?.maxAmplitude ?: 0
        }
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }

        recorder?.start()
        state = State.ON_RECORDING
        soundVisualizerView.startVisualizing(false)
        recordTimeTextView.startCountUp()
    }

    private fun stopRecording() {
        recorder?.run {
            stop()
            release()
        }

        recorder = null
        state = State.AFTER_RECORDING
        soundVisualizerView.stopVisualizing()
        recordTimeTextView.stopCountUp()
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare()
        }

        // 다 재생되면 정지시키기 위해 필요
        player?.setOnCompletionListener {
            stopPlaying()
        }

        player?.start()
        state = State.ON_PLAYING
        soundVisualizerView.startVisualizing(true)
        recordTimeTextView.startCountUp()
    }

    private fun stopPlaying() {
        player?.release()
        player = null
        state = State.AFTER_RECORDING
        soundVisualizerView.stopVisualizing()
        recordTimeTextView.stopCountUp()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestAudioPermission() {
        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted = requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) {
            // 권한을 주지 않으면 앱을 종료한다
            finish()
        }
    }
}