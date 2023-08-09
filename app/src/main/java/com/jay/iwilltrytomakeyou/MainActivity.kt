package com.jay.iwilltrytomakeyou

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jay.iwilltrytomakeyou.database.Alarm
import com.jay.iwilltrytomakeyou.database.AlarmRepository
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var alarmRepository: AlarmRepository


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        alarmRepository= AlarmRepository(this)
        val alarms=alarmRepository.getAllAlarmsFlow()

        alarmRecyclerView=findViewById(R.id.recyclerView)

        alarmAdapter=AlarmAdapter(alarms)
        alarmRecyclerView.adapter=alarmAdapter
        alarmRecyclerView.layoutManager=LinearLayoutManager(this)

        val addAlarmButton:FloatingActionButton=findViewById(R.id.floatingActionButton)
        addAlarmButton.setOnClickListener{
            showAddAlarmDialog()
        }
    }

    //-------------------------------------------------onCreate ends---------------------------------------------------

    private fun showAddAlarmDialog() {

        val dialogView=layoutInflater.inflate(R.layout.dialogue_set_alarm,null)
        val builder=AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setTitle("Add Alarm")

        val labelEditText=dialogView.findViewById<EditText>(R.id.etName)
        val timePicker=dialogView.findViewById<TimePicker>(R.id.timepicker)
        val daysCheckBoxes=dialogView.findViewById<LinearLayout>(R.id.checkBoxes)

        val daysOfWeek= listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")
        daysOfWeek.forEachIndexed { _, day ->
            val checkBox = CheckBox(this)
            checkBox.text = day
            daysCheckBoxes.addView(checkBox)
        }
        builder.setPositiveButton("Add"){dialog,_->
            val label = labelEditText.text.toString()
            val hour=timePicker.hour
            val minute=timePicker.minute

            val selectedDays= mutableListOf<Int>()

            for(i in 0 until daysCheckBoxes.childCount){
                val checkBox=daysCheckBoxes.getChildAt(i) as CheckBox
                if(checkBox.isChecked){
                    selectedDays.add(i)
                }
            }
            val alarmDataTime=Alarm(0,calculateDataTime(hour,minute,selectedDays),
                selectedDays,label, isActive = true)
            alarmRepository.insertAlarm(alarmDataTime)


            alarmAdapter.updateData(alarmRepository.getAllAlarms())
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel"){dialog,_->
            dialog.dismiss()
        }
        builder.create().show()

    }
    //------------------------------ Show add dialog ends ----------------------

    private fun scheduleAlarm(alarmId: Long, alarmDataTime: Long) {
        val alarmManager=getSystemService(Context.ALARM_SERVICE)as AlarmManager
        val intent= Intent(this, AlarmReceiver::class.java)
        intent.putExtra("alarm_id",alarmId)

        val pendingIntent=PendingIntent.getBroadcast(this,alarmId.toInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmDataTime,pendingIntent)

    }
    //-----------------------scheduleAlarm ends --------------------------------

    @SuppressLint("MissingInflatedId")
    private fun showUpdateDialog(alarm: Alarm?) {

        val dialogUpdateView=layoutInflater.inflate(R.layout.dialogue_edit_alarm,null)
        val builder=AlertDialog.Builder(this)
        builder.setView(dialogUpdateView)
        builder.setTitle("Update Alarm")

        val labelUpdateEditText=dialogUpdateView.findViewById<EditText>(R.id.etUpdateName)
        val timeUpdatePicker=dialogUpdateView.findViewById<TimePicker>(R.id.timeUpdatePicker)
        val daysUpdateCheckBoxes=dialogUpdateView.findViewById<LinearLayout>(R.id.updateCheckBoxes)

        labelUpdateEditText.setText(alarm?.label)

        val calendar=Calendar.getInstance()
        calendar.timeInMillis= alarm!!.dateTime

        timeUpdatePicker.hour=calendar.get(Calendar.HOUR_OF_DAY)
        timeUpdatePicker.minute=calendar.get(Calendar.MINUTE)

        val daysOfWeek= listOf("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday")

        daysOfWeek.forEachIndexed { _, day ->
            val checkBox = getChildAt(day) as CheckBox
            checkBox.isChecked=true
        }

        builder.setPositiveButton("Add"){dialog,_->
            val updatedLabel = labelUpdateEditText.text.toString()
            val updatedHour=timeUpdatePicker.hour
            val updatedMinute=timeUpdatePicker.minute

            val updatedDays= mutableListOf<Int>()
            for(i in 0 until daysUpdateCheckBoxes.childCount){
                val checkBox=daysUpdateCheckBoxes.getChildAt(i) as CheckBox
                if(checkBox.isChecked){
                    updatedDays.add(i)
                }
            }

            val updateDateTime= calculateDataTime(updatedHour,updatedMinute,updatedDays)
            val updatedAlarm= Alarm(alarm.id,updateDateTime,updatedDays,updatedLabel, isActive = true)
            alarmRepository.updateAlarms(updatedAlarm)

            alarmAdapter.updateData(alarmRepository.getAllAlarms())
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel"){dialog,_->
            dialog.dismiss()

        }

        builder.create().show()

    }
//-----------------Show update dialog ends----------------


}

private fun getChildAt(day: String) {

}

private fun calculateDataTime(selectedHour: Int, selectedMinute: Int,selectedDays:List<Int>): Long {
    val calendar=Calendar.getInstance()
    val currentDayOfWeek=calendar.get(Calendar.DAY_OF_WEEK)

    calendar.set(Calendar.HOUR_OF_DAY,selectedHour)
    calendar.set(Calendar.MINUTE,selectedMinute)
    calendar.set(Calendar.SECOND,0)


    var daysToAdd=0
    for(i in 1..7){
        val nextDay=(currentDayOfWeek + i ) % 7
        if(selectedDays.contains(nextDay-i)){
            daysToAdd=i
            break
        }
    }
    calendar.add(Calendar.DAY_OF_MONTH,daysToAdd)
    calendar.add(Calendar.HOUR_OF_DAY,selectedHour)
    calendar.add(Calendar.MINUTE,selectedMinute)
    calendar.add(Calendar.SECOND,0)

    if(calendar.timeInMillis <=System.currentTimeMillis()){
        calendar.add(Calendar.WEEK_OF_YEAR,1)
    }
    return calendar.timeInMillis
}
