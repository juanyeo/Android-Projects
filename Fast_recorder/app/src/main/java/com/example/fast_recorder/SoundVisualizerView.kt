package com.example.fast_recorder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.random.Random

class SoundVisualizerView(context: Context, attrs: AttributeSet? = null):
        View(context, attrs) {

    var onRequestCurrentAmplitude: (() -> Int)? = null
    private var isReplaying: Boolean = false
    public var replayingPosition: Int = 0

    @RequiresApi(Build.VERSION_CODES.M)
    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()
        private const val ACTION_INTERVAL = 20L
    }

    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0
    private var drawingAmplitudes: List<Int> = emptyList()
    private var recordingAmplitudes: List<Int> = emptyList()

    private val visualizeRepeatAction: Runnable = object : Runnable {
        override fun run() {
            if (!isReplaying) {
                val currentAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                drawingAmplitudes = listOf(currentAmplitude) + drawingAmplitudes
                recordingAmplitudes = recordingAmplitudes + listOf(currentAmplitude)
            } else {
                replayingPosition++
            }
            // onDraw 다시 호출
            invalidate()
            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler.post(visualizeRepeatAction)
    }

    fun stopVisualizing() {
        replayingPosition = 0
        Log.d("soundData", recordingAmplitudes.toString())
        handler.removeCallbacks(visualizeRepeatAction)
    }

    fun resetVisualizing() {
        drawingAmplitudes = emptyList()
        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d("mem", "온드로우 실행")

        canvas ?: return

        val centerY = drawingHeight / 2f
        var offsetX = drawingWidth.toFloat()

        if (isReplaying) {
            drawingAmplitudes.takeLast(replayingPosition).forEach { amplitude ->
                val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8F
                offsetX -= LINE_SPACE
                if(offsetX < 0) return@forEach

                canvas.drawLine(
                        offsetX,
                        centerY - (lineLength / 2F),
                        offsetX,
                        centerY + (lineLength / 2F),
                        amplitudePaint
                )
            }
        } else {
            drawingAmplitudes.forEach { amplitude ->
                val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8F
                offsetX -= LINE_SPACE
                if(offsetX < 0) return@forEach

                canvas.drawLine(
                        offsetX,
                        centerY - (lineLength / 2F),
                        offsetX,
                        centerY + (lineLength / 2F),
                        amplitudePaint
                )
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingWidth = w
        drawingHeight = h
    }

}