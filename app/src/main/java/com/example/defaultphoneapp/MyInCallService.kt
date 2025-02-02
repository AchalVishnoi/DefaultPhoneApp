package com.example.defaultphoneapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.telecom.Call
import android.telecom.InCallService
import androidx.core.app.NotificationCompat
import com.example.defaultphoneapp.IncomingCallActivity


class MyInCallService : InCallService() {

    companion object {
        var currentCall: Call? = null
    }

    override fun onCallAdded(call: Call) {
        // Handle new call
        currentCall = call
        call.registerCallback(callCallback)

        if (call.state == Call.STATE_RINGING) {
            showIncomingCallNotification(call)

        }
    }

    override fun onCallRemoved(call: Call) {

        currentCall = null
        call.unregisterCallback(callCallback)
    }

    private val callCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            // Handle call state changes
            when (state) {
                Call.STATE_ACTIVE -> {

                }
                Call.STATE_DISCONNECTED -> {

                }
            }
        }
    }

    private fun showIncomingCallNotification(call: Call) {

        val channelId = "incoming_calls"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Incoming Calls",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE),
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create an intent to launch the IncomingCallActivity
        val intent = Intent(this, IncomingCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("CALLER_NUMBER", getContactName(call.details.handle?.schemeSpecificPart.toString())?:call.details.handle?.schemeSpecificPart)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Incoming Call")
            .setContentText("Call from: ${getContactName(call.details.handle?.schemeSpecificPart.toString())?:call.details.handle?.schemeSpecificPart}")
            .setSmallIcon(R.drawable.baseline_call_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(pendingIntent, true)
            .setOngoing(true)
            .build()

        // Show the notification
        notificationManager.notify(1, notification)
    }

    private fun launchIncomingCallActivity(call: Call) {
        val intent = Intent(this, IncomingCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("CALLER_NUMBER", getContactName(call.details.handle?.schemeSpecificPart.toString())?:call.details.handle?.schemeSpecificPart)
        }
        startActivity(intent)
    }

    @SuppressLint("Range")
    private fun getContactName(phoneNumber: String): String? {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)

        var name: String? = null
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
            }
        }
        return name
    }


}