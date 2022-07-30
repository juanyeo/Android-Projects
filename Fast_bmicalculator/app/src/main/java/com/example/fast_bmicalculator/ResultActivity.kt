package com.example.fast_bmicalculator

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class ResultActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val bmiTextView = findViewById<TextView>(R.id.bmiTextView)
        val levelTextView = findViewById<TextView>(R.id.levelTextView)

        val height = intent.getIntExtra("height", 1)
        val weight = intent.getIntExtra("weight", 1)

        val bmi_value = weight / (height / 100.0).pow(2.0)
        val level_value = when {
            bmi_value >= 35.0 -> "고도 비만"
            bmi_value >= 30.0 -> "중정도 비만"
            bmi_value >= 25.0 -> "경도 비만"
            bmi_value >= 23.0 -> "과체중"
            bmi_value >= 18.5 -> "정상체중"
            else -> "저체중"
        }

        bmiTextView.text = String.format("%.3f", bmi_value)
        levelTextView.text = level_value
    }
}