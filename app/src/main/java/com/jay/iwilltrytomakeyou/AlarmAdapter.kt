package com.jay.iwilltrytomakeyou

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jay.iwilltrytomakeyou.database.Alarm
import java.util.Calendar

class AlarmAdapter(private var alarm: List<Alarm>,private val listener:OnAlarmClickListener) :RecyclerView.Adapter< AlarmAdapter.AlarmViewHolder>() {
    private lateinit var alarmManager: AlarmManager
    inner class AlarmViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        private val tvLabel:TextView=itemView.findViewById(R.id.tvName)
        private val tvClock:TextView=itemView.findViewById(R.id.textClock)
        private val tvDays:TextView=itemView.findViewById(R.id.textDays)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private val slider:Switch=itemView.findViewById(R.id.switchOn)

        private val alarmId=System.currentTimeMillis()
        private val alarmDateTime: Calendar =Calendar.getInstance()

        init {
            if (slider.isChecked){
                alarmManager.scheduleAlarm(alarmId,alarmDateTime)
            }else{
                alarmManager.cancelAlarm(alarmId)
            }
        }

        fun bind(alarm:Alarm){
            tvLabel.text=alarm.label
            tvClock.text= alarm.dateTime.toString()
            tvDays.text= alarm.dayOfWeek.toString()
            slider.isChecked=alarm.isActive
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AlarmAdapter.AlarmViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.item_alarm,parent,false)
        return AlarmViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlarmAdapter.AlarmViewHolder, position: Int) {
        holder.bind(alarm[position])
        holder.itemView.setOnClickListener {
            listener.onAlarmClick(alarm)
        }
    }
    override fun getItemCount(): Int=alarm.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAlarms: List<Alarm>) {
        alarm=newAlarms
        notifyDataSetChanged()
    }
    fun deleteAlarm(position: Int){
        val alarm=getItemId(position)
        notifyItemRemoved(alarm.toInt())
    }
    interface OnAlarmClickListener {
        fun onAlarmClick(alarm: List<Alarm>)

    }
}


