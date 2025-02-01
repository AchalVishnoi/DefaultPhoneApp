package com.example.defaultphoneapp

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.telecom.Call
import android.widget.Button
import android.widget.TextView

class OngoingCallActivity : Activity() {

    private lateinit var tvCallerInfo: TextView
    private lateinit var btnEndCall: Button
    private lateinit var btnHold: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ongoing_call)

        tvCallerInfo = findViewById(R.id.tv_caller_info)
        btnEndCall = findViewById(R.id.btn_end_call)
        btnHold = findViewById(R.id.btn_hold)

        val callerNumber = intent.getStringExtra("CALLER_NUMBER")
        tvCallerInfo.text = "Call with: $callerNumber"

        btnEndCall.setOnClickListener {
            // End the call
            MyInCallService.currentCall?.disconnect()
            finish()
        }

        btnHold.setOnClickListener {
            // Put the call on hold
            MyInCallService.currentCall?.hold()
        }
    }
}