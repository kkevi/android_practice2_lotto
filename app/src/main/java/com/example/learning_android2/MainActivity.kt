package com.example.learning_android2

import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private  val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private  val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private  val runButton by lazy { findViewById<Button>(R.id.btn_run) }
    private  val numPick by lazy { findViewById<NumberPicker>(R.id.np_num) }

    private val numTextViewList : List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6),
        )
    }

    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        numPick.minValue = 1
        numPick.maxValue = 45

        initAddButton()
        initRunButton()
        initClearButton()
    }

    private fun initAddButton(){
        addButton.setOnClickListener {
            when {
                didRun -> showToast("초기화를 먼저 해주세요.") //숫자 6개가 꽉 차있을 경우 초기화를 먼저 해야함
                pickNumberSet.size >= 5 -> showToast("숫자는 최대 5개까지만 선택할 수 있습니다.") //숫자를 이미 5개 선택했다면 하나는 랜덤을 돌려야 함
                pickNumberSet.contains(numPick.value) -> showToast("이미 선책한 숫자입니다.") //중복 숫자는 선택할 수 없음
                else -> {
                    val textView = numTextViewList[pickNumberSet.size] //내가 고른 숫자의 길이만큼의 리스트 값들을 선언
                    textView.isVisible = true //gone으로 처리해뒀던 visible을 true로 전환하여 실제 화면에 보여줌
                    textView.text = numPick.value.toString() //안의 값을 value로 전환

                    setNumBack(numPick.value, textView)
                    pickNumberSet.add(numPick.value)
                }
            }
        }
    }

    private  fun setNumBack(number: Int, textView: TextView) { //백그라운드 컬러 지정해주는 함수
        val background = when(number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_grey
            else -> R.drawable.circle_green
        }

        textView.background = ContextCompat.getDrawable(this, background)
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun initRunButton(){
        runButton.setOnClickListener{
            val list = getRandom() //내가 고른 숫자 + 랜덤 숫자가 담긴 list

            didRun = true

            list.forEachIndexed{index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView) //백그라운드 컬러 지정

            }
        }
    }

    private fun getRandom(): List<Int>{
        val numbers = (1..45).filter{it !in pickNumberSet} // pickNumerSet에 포함되어있지 않는 1부터 45까지의 숫자
        return (pickNumberSet + numbers.shuffled().take(6-pickNumberSet.size)).sorted()
        // 내가 이미 고른 숫자 + 내가 고른 숫자를 제외한 숫자 모으 numbers를 shuffled() 함수로 섞은 뒤
        // take() 함수를 통해 6-내가 이미 고른 숫자의 길이 값만큼 가져온다.
        // 오름차순으로 sorting 해주면 끝
    }

    private fun initClearButton(){
        clearButton.setOnClickListener{
            pickNumberSet.clear()
            numTextViewList.forEach{it.isVisible = false}
            didRun = false
            numPick.value = 1
        }
    }
}