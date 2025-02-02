package com.example.defaultphoneapp

import android.os.Build

import android.telecom.Connection
import android.telecom.DisconnectCause
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class MyConnection : Connection() {
    init {
        setConnectionCapabilities(Connection.CAPABILITY_SUPPORT_HOLD)
        setConnectionProperties(Connection.PROPERTY_SELF_MANAGED)
        setAudioModeIsVoip(true)  // Enable VoIP mode for better call quality
    }

    override fun onAnswer() {
        setActive()
    }

    override fun onReject() {
        setDisconnected(DisconnectCause(DisconnectCause.REJECTED))
        destroy()
    }

    override fun onDisconnect() {
        setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
        destroy()
    }
}

