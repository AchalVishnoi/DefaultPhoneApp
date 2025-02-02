package com.example.defaultphoneapp


import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvNumber: TextView = itemView.findViewById(R.id.tv_number)
    private val tvType: TextView = itemView.findViewById(R.id.tv_type)
    private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
    private val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
    private val tvName: TextView = itemView.findViewById(R.id.tv_name)

    fun bind(callLog: CallLog) {

        tvNumber.text = callLog.number
        tvType.text = callLog.type
        tvDate.text = callLog.date
        tvDuration.text = callLog.duration
        tvName.text=callLog.name
    }
}