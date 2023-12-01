package com.cookandroid.diary_recyclerview

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.cookandroid.diary_recyclerview.databinding.TodoActivityMainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TodoActivity : AppCompatActivity() {
    private lateinit var btnAdd: ImageView
    private lateinit var completedSwitch : Switch
    /*private lateinit var
    private lateinit var
    private lateinit var*/

    private val binding: TodoActivityMainBinding by lazy { TodoActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        completedSwitch = findViewById<Switch>(R.id.completedSwitch)
        btnAdd = findViewById<ImageView>(R.id.btnAdd)

        completedSwitch.setOnCheckedChangeListener { _, isChecked ->
            // 스위치 상태가 변경될 때마다 숨김 처리 수행
            hideCheckedItems(binding.todoLinearLayout1)
            hideCheckedItems(binding.todoLinearLayout2)
            hideCheckedItems(binding.todoLinearLayout3)
            hideCheckedItems(binding.todoLinearLayout4)
        }

        btnAdd.setOnClickListener {
            var intent = Intent(applicationContext, AddTodoActivity::class.java)
            startActivity(intent)
        }


        // 날짜 형식 지정
        val formatter = DateTimeFormatter.ofPattern("MM/dd(EEE)")

        // 오늘 날짜를 TextView에 설정
        val todayDateTextView: TextView = findViewById(R.id.todayDateTextView)
        val currentDate = LocalDate.now() //오늘 날짜 계산
        todayDateTextView.text = currentDate.format(formatter)

        // 내일 날짜를 TextView에 설정
        val tomorrowTextView: TextView = findViewById(R.id.tomorrowDateTextView)
        val tomorrowDate = currentDate.plusDays(1) // 내일 날짜 계산
        tomorrowTextView.text = tomorrowDate.format(formatter)

        val todoLinearLayout1 = binding.todoLinearLayout1
        val todoLinearLayout2 = binding.todoLinearLayout2
        val todoLinearLayout3 = binding.todoLinearLayout3
        val todoLinearLayout4 = binding.todoLinearLayout4

        // 데이터베이스 연결 해야 할 부분
        val todoItems1 = listOf(
            TodoItem("2023-11-24","할 일 1 카드 1", false),
            TodoItem("2023-11-24","할 일 2 카드 1", false)
        )

        val todoItems2 = listOf(
            TodoItem("2023-11-24","할 일 1 카드 2", false),
            TodoItem("2023-11-24","할 일 2 카드 2", false)
        )

        val todoItems3 = listOf(
            TodoItem("2023-11-24","할 일 1 카드 3", false),
            TodoItem("2023-11-24","할 일 2 카드 3", false)
        )

        val todoItems4 = listOf(
            TodoItem("2023-11-24","할 일 1 카드 4", false),
            TodoItem("2023-11-24","할 일 2 카드 4", false),
            TodoItem("2023-11-24","할 일 2 카드 4", false),
            TodoItem("2023-11-24","할 일 2 카드 4", false)
        )

        setupToggleView(binding.layoutBtn01, binding.layoutDetail01, binding.layoutBtn01, todoLinearLayout1, todoItems1)
        setupToggleView(binding.layout01, binding.layoutDetail01, binding.layoutBtn01, todoLinearLayout1, todoItems1)

        setupToggleView(binding.layoutBtn02, binding.layoutDetail02, binding.layoutBtn02, todoLinearLayout2, todoItems2)
        setupToggleView(binding.layout02, binding.layoutDetail02, binding.layoutBtn02, todoLinearLayout2, todoItems2)

        setupToggleView(binding.layoutBtn03, binding.layoutDetail03, binding.layoutBtn03, todoLinearLayout3, todoItems3)
        setupToggleView(binding.layout03, binding.layoutDetail03, binding.layoutBtn03, todoLinearLayout3, todoItems3)

        setupToggleView(binding.layoutBtn04, binding.layoutDetail04, binding.layoutBtn04, todoLinearLayout4, todoItems4)
        setupToggleView(binding.layout04, binding.layoutDetail04, binding.layoutBtn04, todoLinearLayout4, todoItems4)
    }

    private fun setupToggleView(
        triggerView: View,
        detailLayout: View,
        rotationView: View,
        linearLayout: LinearLayout,
        todoItems: List<TodoItem>
    ) {
        triggerView.setOnClickListener {
            if (detailLayout.visibility == View.VISIBLE) {
                detailLayout.visibility = View.GONE
                rotationView.animate().apply {
                    duration = 300
                    rotation(0f)
                }
                linearLayout.removeAllViews()
            } else {
                detailLayout.visibility = View.VISIBLE
                rotationView.animate().apply {
                    duration = 300
                    rotation(90f)
                }
                openTodoItems(linearLayout, todoItems)
            }
        }
    }

    private fun openTodoItems(linearLayout: LinearLayout, todoItems: List<TodoItem>) {
        val layoutInflater = LayoutInflater.from(this)

        for (item in todoItems) {
            val todoView = layoutInflater.inflate(R.layout.todo_item, null)
            val contentTextView = todoView.findViewById<TextView>(R.id.todo_TextViewTask)
            val checkBox = todoView.findViewById<CheckBox>(R.id.todo_CheckBox)

            contentTextView.text = item.content

            checkBox.isChecked = item.isChecked

            // 체크 박스의 상태가 변경될 때의 리스너 설정
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // 체크될 때 가운데 줄 추가
                    contentTextView.paintFlags = contentTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    // 체크가 해제될 때 가운데 줄 제거
                    contentTextView.paintFlags = contentTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
            linearLayout.addView(todoView)
        }
    }

    //완료 항목 숨기기
    private fun hideCheckedItems(linearLayout: LinearLayout) {
        // 스위치의 상태 가져오기
        val isSwitchChecked = completedSwitch.isChecked

        // 모든 레이아웃 반복
        for (i in 0 until linearLayout.childCount) {
            val todoView = linearLayout.getChildAt(i) as LinearLayout
            val checkBox = todoView.findViewById<CheckBox>(R.id.todo_CheckBox)

            // 스위치 on && 체크 true면 숨기기
            if (isSwitchChecked && checkBox.isChecked) {
                todoView.visibility = View.GONE
            } else {
                // 스위치가 off || 체크 false면 보이기
                todoView.visibility = View.VISIBLE
            }
        }
    }


}


