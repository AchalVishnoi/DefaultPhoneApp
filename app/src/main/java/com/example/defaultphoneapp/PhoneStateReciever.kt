package com.example.defaultphoneapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class PhoneStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

        if (state == TelephonyManager.EXTRA_STATE_RINGING) {
            // An incoming call is ringing
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            // Start your custom incoming call activity
            val incomingCallIntent = Intent(context, IncomingCallActivity::class.java)
            incomingCallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            incomingCallIntent.putExtra("CALLER_NUMBER", incomingNumber)
            context.startActivity(incomingCallIntent)
        }
    }
}
