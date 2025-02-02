package com.example.defaultphoneapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.widget.Button
import android.widget.TextView

class IncomingCallActivity : Activity() {

    private lateinit var tvCallerInfo: TextView
    private lateinit var btnAccept: Button
    private lateinit var btnReject: Button



    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incoming_call)

        tvCallerInfo = findViewById(R.id.tv_caller_info)
        btnAccept = findViewById(R.id.btn_accept)
        btnReject = findViewById(R.id.btn_reject)

        val callerNumber = intent.getStringExtra("CALLER_NUMBER")
        tvCallerInfo.text = "Call from: $callerNumber"

        btnAccept.setOnClickListener {
            MyInCallService.currentCall?.answer(Call.STATE_ACTIVE)
            val intent = Intent(this, OngoingCallActivity::class.java).apply {

                putExtra("CALLER_NUMBER", callerNumber)
            }
            startActivity(intent)
            finish()
        }

        btnReject.setOnClickListener {

            MyInCallService.currentCall?.disconnect()

            finish()
        }
    }
}