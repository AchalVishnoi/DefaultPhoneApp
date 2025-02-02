package com.example.defaultphoneapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
class ContactsAdapter(private val contacts: List<CallLog>) : RecyclerView.Adapter<CallLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_call_log, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.bind(contacts[position])

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DialerActivity::class.java)
            intent.putExtra("phone_number", contacts[position].number)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}

