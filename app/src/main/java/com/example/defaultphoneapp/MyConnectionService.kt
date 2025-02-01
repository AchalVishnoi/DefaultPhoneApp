package com.example.defaultphoneapp

import android.annotation.SuppressLint
import android.net.Uri
import android.telecom.*
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager

class MyConnectionService : ConnectionService() {

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest
    ): Connection {
        // Create a new outgoing connection
        val connection = MyConnection()
        connection.setConnectionProperties(Connection.PROPERTY_SELF_MANAGED)
        connection.setCallerDisplayName(request.address?.schemeSpecificPart, Connection.PRESENTATION_ALLOWED)
        connection.setAddress(request.address, Connection.PRESENTATION_ALLOWED)
        connection.setVideoState(request.videoState)
        return connection
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest
    ): Connection {
        // Create a new incoming connection
        val connection = MyConnection()
        connection.setConnectionProperties(Connection.PROPERTY_SELF_MANAGED)
        connection.setCallerDisplayName(request.address?.schemeSpecificPart, Connection.PRESENTATION_ALLOWED)
        connection.setAddress(request.address, Connection.PRESENTATION_ALLOWED)
        connection.setVideoState(request.videoState)
        return connection
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest
    ) {
        // Handle outgoing call failure
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest
    ) {
        // Handle incoming call failure
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
    }
}