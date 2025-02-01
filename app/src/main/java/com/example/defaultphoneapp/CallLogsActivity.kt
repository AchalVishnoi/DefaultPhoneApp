package com.example.defaultphoneapp

import android.app.Activity
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CallLogsActivity : Activity() {

    private lateinit var rvCallLogs: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_logs)

        rvCallLogs = findViewById(R.id.rv_call_logs)
        rvCallLogs.layoutManager = LinearLayoutManager(this)

        // Fetch call logs
        val callLogs = fetchCallLogs()
        rvCallLogs.adapter = CallLogsAdapter(callLogs)
    }

    private fun fetchCallLogs(): List<com.example.defaultphoneapp.CallLog> {
        val callLogs = mutableListOf<com.example.defaultphoneapp.CallLog>()

        // Query the call log
        val cursor: Cursor? = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

            while (it.moveToNext()) {
                val number = it.getString(numberIndex)
                val type = when (it.getInt(typeIndex)) {
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    else -> "Unknown"
                }
                val date = it.getLong(dateIndex)
                val duration = it.getString(durationIndex)

                // Format the date
                val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(Date(date))

                // Add to the list
                callLogs.add(CallLog(number, type, formattedDate, duration))
            }
        }

        return callLogs
    }
}