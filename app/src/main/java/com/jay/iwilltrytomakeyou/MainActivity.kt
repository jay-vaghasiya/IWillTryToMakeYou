package com.jay.iwilltrytomakeyou

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jay.iwilltrytomakeyou.database.Alarm
import com.jay.iwilltrytomakeyou.database.AlarmViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(){

    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var alarmRecyclerView: RecyclerView
    private val alarmViewModel: AlarmViewModel by viewModels()
    private lateinit var alarmManager: AlarmManager
    private var alarms= mutableListOf<Alarm>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManager = AlarmManager(this)
        alarmRecyclerView = findViewById(R.id.recyclerView)

        alarmRecyclerView.layoutManager = LinearLayoutManager(this)

        alarmAdapter = AlarmAdapter(alarms,alarmViewModel)
        alarmRecyclerView.adapter = alarmAdapter

        lifecycleScope.launch {
            alarmViewModel.allAlarmLiveData.collect { alarms ->
                alarmAdapter.updateData(alarms)
            }
        }

        val addAlarmButton: FloatingActionButton = findViewById(R.id.floatingActionButton)
        addAlarmButton.setOnClickListener {
            showAddAlarmDialog()
        }

    }

    //-------------------------------------------------onCreate ends--------------------------------
    private fun showAddAlarmDialog() {

        val dialogView = layoutInflater.inflate(R.layout.dialogue_set_alarm, null)

        val labelEditText = dialogView.findViewById<EditText>(R.id.etName)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.timepicker)


        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Alarm")
            .setPositiveButton("Add") { dialog, _ ->
                // set up positive button
                val label = labelEditText.text.toString()
                val hour = timePicker.hour
                val minute = timePicker.minute
                val timeToHitAlarm= calculateDataTime(hour,minute)
                val newAlarm=Alarm(
                0, timeToHitAlarm, label, isActive = true
                )

                val alarmDataTime=Calendar.getInstance()
                alarmDataTime.timeInMillis = timeToHitAlarm

                alarmViewModel.insertAlarm(newAlarm)
                alarmManager.scheduleAlarm(timeToHitAlarm, alarmDataTime)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
        alarmAdapter.updateData(alarms)
    }
    //------------------------------ Show add dialog ends ----------------------

    private fun calculateDataTime(
        selectedHour: Int, selectedMinute: Int
    ): Long {
        val tvClock: TextView? = findViewById(R.id.textClock)
        val calendar = Calendar.getInstance()
        val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault())

        tvClock?.text = "$formattedTime"

        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }
        return calendar.timeInMillis
    }

}




