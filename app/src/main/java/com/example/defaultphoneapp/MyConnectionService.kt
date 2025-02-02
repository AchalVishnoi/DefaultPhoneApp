package com.example.defaultphoneapp

import android.net.Uri
import android.os.Build
import android.telecom.*
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class MyConnectionService : ConnectionService() {

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest
    ): Connection {
        val connection = MyConnection()
        connection.setConnectionProperties(Connection.PROPERTY_SELF_MANAGED)

        request.address?.let { address ->
            connection.setAddress(address, TelecomManager.PRESENTATION_ALLOWED)
            connection.setCallerDisplayName(
                address.schemeSpecificPart ?: "Unknown",
                TelecomManager.PRESENTATION_ALLOWED
            )
        }

        connection.setVideoState(request.videoState)
        connection.setDialing() // Show dialing UI
        connection.setActive()  // Move to active state
        return connection
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest
    ): Connection {
        val connection = MyConnection()
        connection.setConnectionProperties(Connection.PROPERTY_SELF_MANAGED)

        request.address?.let { address ->
            connection.setAddress(address, TelecomManager.PRESENTATION_ALLOWED)
            connection.setCallerDisplayName(
                address.schemeSpecificPart ?: "Unknown",
                TelecomManager.PRESENTATION_ALLOWED
            )
        }

        connection.setVideoState(request.videoState)
        connection.setRinging() // Ensure phone rings
        return connection
    }
}
