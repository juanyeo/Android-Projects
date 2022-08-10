package com.example.fast_tomatotimer

import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    private val remainMinutesTextView: TextView by lazy {
        findViewById<TextView>(R.id.remainMinutesTextView)
    }

    private val remainSecondsTextView: TextView by lazy {
        findViewById<TextView>(R.id.remainSecondsTextView)
    }

    private val timeSeekBar: SeekBar by lazy {
        findViewById<SeekBar>(R.id.timeSeekBar)
    }

    private var currentCountDownTimer: CountDownTimer? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val soundPool = SoundPool.Builder().build()

    private var tickingSoundID: Int? = null

    private var bellSoundID: Int? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSeekBar()
        initSounds()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPause() {
        super.onPause()
        soundPool.autoPause()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        soundPool.autoResume()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initSounds() {
        tickingSoundID = soundPool.load(this, R.raw.timer_ticking, 1)
        bellSoundID = soundPool.load(this, R.raw.timer_bell, 1)
    }

    private fun initSeekBar() {
        timeSeekBar.setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if(fromUser) updateRemainTime(progress * 60 * 1000L)
                }

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    stopCountDown()
                }

                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar ?: return

                    if (seekBar.progress == 0) {
                        stopCountDown()
                    } else {
                        startCountDown()
                    }
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startCountDown() {
        currentCountDownTimer = createCountDownTimer(timeSeekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()

        tickingSoundID?.let { soundID ->
            soundPool.play(soundID, 1F, 1F, 0, -1, 1F)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun stopCountDown() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        soundPool.autoPause()
    }

    private fun createCountDownTimer(initialMillis: Long): CountDownTimer {
        return object: CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
                updateSeekBar(millisUntilFinished)
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onFinish() {
                updateRemainTime(0)
                updateSeekBar(0)

                bellSoundID?.let { soundID ->
                    soundPool.play(soundID, 1F, 1F, 0, 0, 1F)
                }
            }
        }
    }

    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        remainMinutesTextView.text = "%02d'".format(remainSeconds / 60)
        remainSecondsTextView.text = "%02d".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        timeSeekBar.progress = (remainMillis / 1000 / 60).toInt()
    }
}