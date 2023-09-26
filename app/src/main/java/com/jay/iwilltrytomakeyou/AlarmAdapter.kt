package com.jay.iwilltrytomakeyou

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.jay.iwilltrytomakeyou.database.Alarm
import com.jay.iwilltrytomakeyou.database.AlarmViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AlarmAdapter(
    private var alarm: List<Alarm>,
    private val alarmViewModel: AlarmViewModel,
    private val context: Context
) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    inner class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLabel: MaterialTextView = itemView.findViewById(R.id.tvName)
        private val tvClock: MaterialTextView = itemView.findViewById(R.id.textClock)
        private val slider: MaterialTextView = itemView.findViewById(R.id.switchOn)

        fun bind(alarm: Alarm) {
            tvLabel.text = alarm.label
            tvClock.text = timeLongToString(alarm.dateTime)
            val alarmManager = AlarmManager(context)
            slider.setOnClickListener {
                val alarmId = alarm.id
                alarmManager.cancelAlarm(alarmId)
                alarmViewModel.deleteAlarm(alarm)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmAdapter.AlarmViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlarmAdapter.AlarmViewHolder, position: Int) {
        holder.bind(alarm[position])

    }

    override fun getItemCount(): Int = alarm.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAlarms: List<Alarm>) {
        alarm = newAlarms
        notifyDataSetChanged()
    }

    private fun timeLongToString(time: Long): String {
        val calendar = Calendar.getInstance()
        val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        calendar.time = Date(time)
        return formattedTime.format(calendar.time)

    }


}


