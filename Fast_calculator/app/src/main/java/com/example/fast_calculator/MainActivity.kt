package com.example.fast_calculator

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.fast_calculator.model.History
import java.lang.NumberFormatException
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }

    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }

    private val historyLayout: View by lazy {
        findViewById<View>(R.id.historyLayout)
    }

    private val historyLinearLayout: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.historyLinearLayout)
    }

    lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // database 변수에 데이터베이스 생성
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun buttonAction(view: View) {
        val id = view.id
        when(id) {
            R.id.button0 -> numberButtonAction("0")
            R.id.button1 -> numberButtonAction("1")
            R.id.button2 -> numberButtonAction("2")
            R.id.button3 -> numberButtonAction("3")
            R.id.button4 -> numberButtonAction("4")
            R.id.button5 -> numberButtonAction("5")
            R.id.button6 -> numberButtonAction("6")
            R.id.button7 -> numberButtonAction("7")
            R.id.button8 -> numberButtonAction("8")
            R.id.button9 -> numberButtonAction("9")
            R.id.modulusButton -> operatorButtonAction("%")
            R.id.divideButton -> operatorButtonAction("/")
            R.id.multiplyButton -> operatorButtonAction("*")
            R.id.plusButton -> operatorButtonAction("+")
            R.id.minusButton -> operatorButtonAction("-")
        }
    }

    private fun numberButtonAction(number: String) {
        val expressionTexts = expressionTextView.text.split(" ")

        if (expressionTexts.isNotEmpty() && expressionTexts.last().length >= 15) {
            Toast.makeText(this, "숫자는 15자리를 넘어갈 수 없습니다", Toast.LENGTH_SHORT).show()
            return
        }

        if (expressionTexts.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)

        if (expressionTexts.size == 3) resultTextView.text = calculateExpression()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun operatorButtonAction(operator: String) {
        val expressionTexts = expressionTextView.text.split(" ")

        if (expressionTextView.text.isEmpty()) {
            Toast.makeText(this, "숫자를 먼저 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }

        if (expressionTexts.size > 1 && expressionTexts.last().isEmpty()) {
            Toast.makeText(this, "두 개의 연산자를 연달아 사용할 수 없습니다", Toast.LENGTH_SHORT).show()
            return
        }

        if (expressionTexts.size > 1 && expressionTexts.last().isNotEmpty()) {
            Toast.makeText(this, "연산자는 한 개까지만 사용할 수 있습니다", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(" ${operator} ")

        val ssb = SpannableStringBuilder(expressionTextView.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            expressionTextView.text.length -2,
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        expressionTextView.text = ssb
    }

    private fun calculateExpression(): String {
        val expressionTexts = expressionTextView.text.split(" ")

        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) return ""

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val operator = expressionTexts[1]

        return when(operator) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun resultButtonAction(view: View) {
        val expressionTexts = expressionTextView.text.split(" ")

        if (expressionTexts.size == 1) resultTextView.text = expressionTexts[0]

        if (expressionTexts.size == 3 && expressionTexts.last().isNumber()) {
            val expressionText = expressionTextView.text.toString()
            val resultText = calculateExpression()
            resultTextView.text = resultText

            // 데이터베이스에 데이터 입력
            Thread(Runnable {
                database.historyDao().insertHistory(History(null, expressionText, resultText))
            }).start()
        }
    }

    fun clearButtonAction(view: View) {
        expressionTextView.text = ""
        resultTextView.text = ""
    }

    fun historyButtonAction(view: View) {
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews()

        // 데이터베이스에서 데이터 가져오기
        Thread(Runnable {
            database.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val historyRow = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyRow.findViewById<TextView>(R.id.expressionTextViewRow).text = it.expression
                    historyRow.findViewById<TextView>(R.id.resultTextViewRow).text = "= ${it.result}"

                    historyLinearLayout.addView(historyRow)
                }
            }
        }).start()
    }

    fun historyCloseButtonAction(view: View) {
        historyLayout.isVisible = false
    }

    fun historyClearButtonAction(view: View) {
        historyLinearLayout.removeAllViews()

        // 데이터베이스 데이터 삭제
        Thread(Runnable {
            database.historyDao().deleteAll()
        }).start()
    }
}

private fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}
