package com.jay.iwilltrytomakeyou

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jay.iwilltrytomakeyou.database.Alarm
import com.jay.iwilltrytomakeyou.database.AlarmViewModel

class AlarmAdapter(private var alarm: List<Alarm>,private val alarmViewModel: AlarmViewModel) :RecyclerView.Adapter< AlarmAdapter.AlarmViewHolder>() {

    inner class AlarmViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        private val tvLabel:TextView=itemView.findViewById(R.id.tvName)
        private val tvClock:TextView=itemView.findViewById(R.id.textClock)
        private val slider: Button =itemView.findViewById(R.id.switchOn)
        fun bind(alarm:Alarm){
            tvLabel.text=alarm.label
            tvClock.text= alarm.dateTime.toString()
            slider.setOnClickListener{
                alarmViewModel.deleteAlarm(alarm)
            }
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

    }
    override fun getItemCount(): Int=alarm.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newAlarms: List<Alarm>) {
        alarm=newAlarms
        notifyDataSetChanged()
    }


}


