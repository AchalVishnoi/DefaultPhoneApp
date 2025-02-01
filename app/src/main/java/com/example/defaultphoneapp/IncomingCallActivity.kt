package com.example.defaultphoneapp

import android.app.Activity
import android.os.Bundle
import android.telecom.Call
import android.widget.Button
import android.widget.TextView

class IncomingCallActivity : Activity() {

    private lateinit var tvCallerInfo: TextView
    private lateinit var btnAccept: Button
    private lateinit var btnReject: Button

    lateinit var MyInCallService : MyInCallService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_call)

        tvCallerInfo = findViewById(R.id.tv_caller_info)
        btnAccept = findViewById(R.id.btn_accept)
        btnReject = findViewById(R.id.btn_reject)

        val callerNumber = intent.getStringExtra("CALLER_NUMBER")
        tvCallerInfo.text = "Call from: $callerNumber"

        btnAccept.setOnClickListener {
            // Accept the call
            MyInCallService.currentCall?.answer(Call.STATE_ACTIVE)
            finish()
        }

        btnReject.setOnClickListener {
            // Reject the call
            MyInCallService.currentCall?.disconnect()
            finish()
        }
    }
}