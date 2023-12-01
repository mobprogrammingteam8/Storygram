package com.cookandroid.diary_recyclerview

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTodoActivity : Activity() {
    private lateinit var dateButton: Button
    private lateinit var btnBack: Button
    private lateinit var btnOK: Button
    private lateinit var textTask : EditText
    private lateinit var dbHelper: DBHelper

    private val selectedDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_todo)

        /*dbHelper = DBHelper(this)*/

        dateButton = findViewById<Button>(R.id.todo_dateButton)
        btnBack = findViewById<Button>(R.id.todo_btnBack)
        btnOK = findViewById<Button>(R.id.todo_btnOK)
        textTask = findViewById<EditText>(R.id.todo_EditTextTask)

        updateDateButton() //현재 날짜로 초기화

        // dateButton 클릭 시 DatePickerDialog 표시
        dateButton.setOnClickListener {
            showDatePickerDialog()
        }

        //뒤로가기 누르면 추가화면 인텐트 종료
        btnBack.setOnClickListener{
            finish()
        }

        //확인 클릭 시 데이터베이스에 날짜, 할 일 내용 저장
        /*btnOK.setOnClickListener{
            // 날짜 및 할 일 내용 가져오기
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
            val todoTask = textTask.text.toString()  // EditText의 내용 가져오기

            // 데이터베이스에 저장
            dbHelper.insertTodoItem(currentDate, todoTask)

        }*/
    }


    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                // 사용자가 날짜를 선택하면 Calendar 객체 업데이트
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, monthOfYear)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // 선택한 날짜로 dateButton 업데이트
                updateDateButton()
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun updateDateButton() {
        // 선택된 날짜를 dateButton에 표시
        val dateFormat = SimpleDateFormat("yyyy.MM.dd.(EEE)", Locale.KOREA)
        dateButton.text = dateFormat.format(selectedDate.time)
    }
}