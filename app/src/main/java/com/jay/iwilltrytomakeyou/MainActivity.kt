package com.jay.iwilltrytomakeyou

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.jay.iwilltrytomakeyou.database.Alarm
import com.jay.iwilltrytomakeyou.database.AlarmViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var alarmAdapter: AlarmAdapter
    private lateinit var alarmRecyclerView: RecyclerView
    private val alarmViewModel: AlarmViewModel by viewModels()
    private lateinit var alarmManager: AlarmManager

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //schedule exact alarm permission
        if (checkPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM, this)) {
            alarmManager = AlarmManager(this)
        } else {
            requestPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM, this)
        }

        //notification permission
        if (checkNotificationPermission(this)) {
            Toast.makeText(this, "GOOD", Toast.LENGTH_SHORT).show()
        } else {
            requestNotificationPermission(this)
        }

        // Ignore battery optimizations
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val packageName = packageName
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager

            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }

        alarmRecyclerView = findViewById(R.id.recyclerView)
        alarmRecyclerView.layoutManager = LinearLayoutManager(this)

        alarmAdapter = AlarmAdapter(alarmViewModel, this)
        alarmRecyclerView.adapter = alarmAdapter

        val text = findViewById<MaterialTextView>(R.id.empty)

        alarmAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                checkEmpty()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                checkEmpty()
            }

            fun checkEmpty() {
                text.visibility = (if (alarmAdapter.itemCount == 0) View.VISIBLE else View.GONE)
            }
        })

        lifecycleScope.launch {
            alarmViewModel.allAlarmLiveData.collect { alarms ->
                alarmAdapter.updateData(alarms)
            }
        }

        val addAlarmButton: ExtendedFloatingActionButton = findViewById(R.id.btAddForm)
        addAlarmButton.setOnClickListener {
            showAddAlarmDialog()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                alarmManager = AlarmManager(this)
            } else {
                Toast.makeText(this, "TFYS", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddAlarmDialog() {

        val dialogBuilder = Dialog(this)
        dialogBuilder.setContentView(R.layout.dialogue_set_alarm)
        alarmManager = AlarmManager(this)

        val labelEditText = dialogBuilder.findViewById<EditText>(R.id.etName)
        val timePicker = dialogBuilder.findViewById<TimePicker>(R.id.timepicker)
        val cancel = dialogBuilder.findViewById<MaterialButton>(R.id.btCancel)
        val submit = dialogBuilder.findViewById<MaterialButton>(R.id.btSubmit)

        submit.setOnClickListener {
            val label = labelEditText.text.toString()
            val hour = timePicker.hour
            val minute = timePicker.minute
            val unixTimestamp = calculateDataTime(hour, minute)

            val newAlarm = Alarm(
                0, unixTimestamp, label
            )

            val alarmDataTime = Calendar.getInstance()
            alarmDataTime.timeInMillis = unixTimestamp

            lifecycleScope.launch {
                alarmViewModel.insertAlarm(newAlarm)
            }
            alarmManager.scheduleAlarm(unixTimestamp, alarmDataTime, newAlarm)
            dialogBuilder.dismiss()
        }
        cancel.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.show()
    }

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

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}