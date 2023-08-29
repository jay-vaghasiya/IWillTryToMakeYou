package com.jay.iwilltrytomakeyou

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jay.iwilltrytomakeyou.database.Alarm
import com.jay.iwilltrytomakeyou.database.AlarmViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var alarmRecyclerView: RecyclerView
    private val alarmViewModel: AlarmViewModel by viewModels()
    private lateinit var alarmManager: AlarmManager
    private var alarms = mutableListOf<Alarm>()
    private val PERMISSSION_TO_SHOW_NOTIFICATIONS=1


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(checkNotificationPemission()){
            alarmManager = AlarmManager(this)
        }else{
            requestNotificationPermission()
        }
        alarmRecyclerView = findViewById(R.id.recyclerView)

        alarmRecyclerView.layoutManager = LinearLayoutManager(this)

        alarmAdapter = AlarmAdapter(alarms, alarmViewModel, this)
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
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNotificationPemission(): Boolean {
        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.POST_NOTIFICATIONS)==
                PackageManager.PERMISSION_GRANTED
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
            PERMISSSION_TO_SHOW_NOTIFICATIONS)
    }

    //-------------------------------------------------onCreate ends--------------------------------
    private fun showAddAlarmDialog() {

        val dialogView = layoutInflater.inflate(R.layout.dialogue_set_alarm, null)
        alarmManager = AlarmManager(this)

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
                val unixTimestamp = calculateDataTime(hour, minute)

                val newAlarm = Alarm(
                    0, unixTimestamp, label, isActive = true
                )

                val alarmDataTime = Calendar.getInstance()
                alarmDataTime.timeInMillis = unixTimestamp

                alarmViewModel.insertAlarm(newAlarm)
                alarmManager.scheduleAlarm(unixTimestamp, alarmDataTime)
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
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
        calendar.set(Calendar.MINUTE, selectedMinute)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }
        return calendar.timeInMillis

    }

// ------------------------------------calculate time end------------------------------------------
}




