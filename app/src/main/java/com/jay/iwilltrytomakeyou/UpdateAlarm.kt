package com.jay.iwilltrytomakeyou

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.jay.iwilltrytomakeyou.database.Alarm
import com.jay.iwilltrytomakeyou.database.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateAlarm : AppCompatActivity() {

    lateinit var alarm: Alarm
    private lateinit var alarmViewModel:AlarmViewModel
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_alarm)

        val labelUpdateEditText = findViewById<EditText>(R.id.etUpdateName)
        val timeUpdatePicker = findViewById<TimePicker>(R.id.timeUpdatePicker)



        labelUpdateEditText.setText(alarm.label)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarm.dateTime

        timeUpdatePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
        timeUpdatePicker.minute = calendar.get(Calendar.MINUTE)

            val updatedLabel = labelUpdateEditText.text.toString()
            val updatedHour = timeUpdatePicker.hour
            val updatedMinute = timeUpdatePicker.minute
            val updatedDays = mutableListOf<String>()

            val updatedAlarm= Alarm(
                0, calculateDataTime(updatedHour, updatedMinute),
                updatedDays, updatedLabel, isActive = true
            )

            alarmViewModel.updateAlarm(updatedAlarm)

        }
    private fun calculateDataTime(
        selectedHour: Int, selectedMinute: Int
    ): Long {
        val tvClock: TextView?=findViewById(R.id.textClock)
        val calendar = Calendar.getInstance()
        val timeFormat= SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime=timeFormat.format(calendar.time)

        calendar.get(Calendar.DAY_OF_WEEK)
        tvClock?.text=formattedTime

        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)
        calendar.set(Calendar.SECOND, 0)


        val daysToAdd = 0

        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd)
        calendar.add(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.add(Calendar.MINUTE, selectedMinute)
        calendar.add(Calendar.SECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }

        return calendar.timeInMillis
    }
}