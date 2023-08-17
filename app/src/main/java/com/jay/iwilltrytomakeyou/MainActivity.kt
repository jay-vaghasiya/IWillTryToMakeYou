package com.jay.iwilltrytomakeyou

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jay.iwilltrytomakeyou.database.Alarm
import com.jay.iwilltrytomakeyou.database.AlarmViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), AlarmAdapter.OnAlarmClickListener {

    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var alarmManager: AlarmManager
    private var alarms= mutableListOf<Alarm>()


    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)
        alarmManager= AlarmManager(this)

        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
        alarmRecyclerView = findViewById(R.id.recyclerView)

        alarmRecyclerView.layoutManager=LinearLayoutManager(this)

        alarmAdapter= AlarmAdapter(alarms,this)
        alarmRecyclerView.adapter=alarmAdapter
        lifecycleScope.launch {
            alarmViewModel.allAlarmLiveData.collect{alarms->
                alarmAdapter.updateData(alarms)
            }
        }

        val addAlarmButton: FloatingActionButton = findViewById(R.id.floatingActionButton)
        addAlarmButton.setOnClickListener {
            showAddAlarmDialog()
        }
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(alarmAdapter))
        itemTouchHelper.attachToRecyclerView(alarmRecyclerView)

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
                val selectedDays = mutableListOf<String>()

                val monday=findViewById<CheckBox>(R.id.tvMonday)
                val tuesday=findViewById<CheckBox>(R.id.tvTuesday)
                val wednesday=findViewById<CheckBox>(R.id.tvWednesday)
                val thursday=findViewById<CheckBox>(R.id.tvThursday)
                val friday=findViewById<CheckBox>(R.id.tvFriday)
                val saturday=findViewById<CheckBox>(R.id.tvSaturday)
                val sunday=findViewById<CheckBox>(R.id.tvSunday)

                val alarmId=System.currentTimeMillis()
                val alarmDataTime=Calendar.getInstance()

                val selectedDaysTextView=findViewById<TextView>(R.id.textDays)

                val checkBoxListener= CompoundButton.OnCheckedChangeListener{ checkbox, isChecked ->
                    val day=checkbox.text.toString()
                    if(isChecked){
                        selectedDays.add(day)
                    }else{
                        selectedDays.remove(day)
                    }
                    selectedDaysTextView.text=selectedDays.joinToString { ", " }
                }
                monday?.setOnCheckedChangeListener(checkBoxListener)
                tuesday?.setOnCheckedChangeListener(checkBoxListener)
                wednesday?.setOnCheckedChangeListener(checkBoxListener)
                thursday?.setOnCheckedChangeListener(checkBoxListener)
                friday?.setOnCheckedChangeListener(checkBoxListener)
                saturday?.setOnCheckedChangeListener(checkBoxListener)
                sunday?.setOnCheckedChangeListener(checkBoxListener)

                val newAlarm=Alarm(
                0, calculateDataTime(hour, minute),
                selectedDays, label, isActive = true
                )
                alarmManager.scheduleAlarm(alarmId, alarmDataTime)
                alarmViewModel.insertAlarm(newAlarm)
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
        val tvClock:TextView?=findViewById(R.id.textClock)
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

    override fun onAlarmClick(alarm:List<Alarm>) {
        val intent=Intent(this,UpdateAlarm::class.java)
        startActivity(intent)
    }

}



