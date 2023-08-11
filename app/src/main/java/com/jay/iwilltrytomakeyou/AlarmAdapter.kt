package com.jay.iwilltrytomakeyou

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jay.iwilltrytomakeyou.database.Alarm

class AlarmAdapter:RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    private var alarm:List<Alarm> = emptyList()

    inner class AlarmViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        private val tvLabel:TextView=itemView.findViewById(R.id.tvName)
        private val tvClock:TextView=itemView.findViewById(R.id.textClock)
        private val tvDays:TextView=itemView.findViewById(R.id.textDays)
        private val tvMore:TextView=itemView.findViewById(R.id.tvMore)
        private val slider:Switch=itemView.findViewById(R.id.switchOn)

        fun bind(alarm:Alarm){
            tvLabel.text=alarm.label
            tvClock.text= alarm.dateTime.toString()
            tvDays.text= alarm.dayOfWeek.toString()
            tvMore.text=alarm.label
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
    }
    override fun getItemCount(): Int=alarm.size

    fun updateData(newAlarms: List<Alarm>) {
        alarm=newAlarms
        notifyDataSetChanged()
    }
}