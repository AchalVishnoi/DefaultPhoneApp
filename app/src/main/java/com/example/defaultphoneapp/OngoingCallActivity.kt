package com.example.defaultphoneapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OngoingCallActivity : AppCompatActivity() {

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

            MyInCallService.currentCall?.disconnect()

            val intent = Intent(this, DialerActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnHold.setOnClickListener {

            MyInCallService.currentCall?.hold()
        }
    }
}
