package com.example.fast_randomnumbers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val addButton: Button by lazy {
        findViewById<Button>(R.id.addButton)
    }

    private val clearButton: Button by lazy {
        findViewById<Button>(R.id.clearButton)
    }

    private val runButton: Button by lazy {
        findViewById<Button>(R.id.runButton)
    }

    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val numberTextViewList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.firstNumberTextView),
            findViewById<TextView>(R.id.secondNumberTextView),
            findViewById<TextView>(R.id.thirdNumberTextView),
            findViewById<TextView>(R.id.fourthNumberTextView),
            findViewById<TextView>(R.id.fifthNumberTextView),
            findViewById<TextView>(R.id.sixthNumberTextView)
        )
    }

    private var didRun = false

    private val pickedNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initAddButton()
        initClearButton()
        initRunButton()
    }

    private fun initAddButton() {
        addButton.setOnClickListener {
            if (didRun) {
                Toast.makeText(this, "초기화 후에 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickedNumberSet.size >= 5) {
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pickedNumberSet.contains(numberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val textView = numberTextViewList[pickedNumberSet.size]

            textView.isVisible = true
            textView.text = numberPicker.value.toString()

            setNumberBackground(numberPicker.value, textView)

            pickedNumberSet.add(numberPicker.value)
        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            didRun = false
            pickedNumberSet.clear()
            numberTextViewList.forEach {
                it.isVisible = false
            }
        }
    }

    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandomNumber()

            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]

                textView.isVisible = true
                textView.text = number.toString()

                setNumberBackground(number, textView)
            }

            didRun = true
        }
    }

    private fun getRandomNumber(): List<Int> {
        val numberList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (pickedNumberSet.contains(i)) continue
                this.add(i)
            }
        }

        numberList.shuffle()

        val newList = pickedNumberSet.toList() + numberList.subList(0, 6 - pickedNumberSet.size)

        return newList.sorted()
    }

    private fun setNumberBackground(number: Int, textView: TextView) {
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
        }
    }
}