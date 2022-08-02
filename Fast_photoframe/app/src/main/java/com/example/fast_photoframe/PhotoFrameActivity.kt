package com.example.fast_photoframe

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private val backgroundImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundImageView)
    }

    private val foregroundImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.foregroundImageView)
    }

    private var size = 0
    private var imageUriList = mutableListOf<Uri>()

    private var timer: Timer? = null
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        getImageUriFromIntent()
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    private fun getImageUriFromIntent() {
        size = intent.getIntExtra("uriListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("image$i")?.let {
                imageUriList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 3 * 1000) {
            runOnUiThread {
                backgroundImageView.setImageURI(imageUriList[currentIndex])

                val nextIndex = if (currentIndex + 1 >= size) 0 else currentIndex + 1

                foregroundImageView.alpha = 0f
                foregroundImageView.setImageURI(imageUriList[nextIndex])
                foregroundImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentIndex = nextIndex
            }
        }
    }
}