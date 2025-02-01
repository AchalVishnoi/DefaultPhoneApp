package com.example.defaultphoneapp

import android.telecom.Connection
import android.telecom.DisconnectCause

class MyConnection : Connection() {
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
