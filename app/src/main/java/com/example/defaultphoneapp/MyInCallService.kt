
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
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
            launchIncomingCallActivity(call)
        }
    }

    override fun onCallRemoved(call: Call) {
        // Handle call removal
        currentCall = null
        call.unregisterCallback(callCallback)
    }

    private val callCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            // Handle call state changes
            when (state) {
                Call.STATE_ACTIVE -> {
                    // Call is active
                }
                Call.STATE_DISCONNECTED -> {
                    // Call ended
                }
            }
        }
    }

    private fun showIncomingCallNotification(call: Call) {
        // Create a notification channel for incoming calls
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
            putExtra("CALLER_NUMBER", call.details.handle?.schemeSpecificPart)
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
            .setContentText("Call from: ${call.details.handle?.schemeSpecificPart}")
            .setSmallIcon(R.drawable.ic_call)
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
            putExtra("CALLER_NUMBER", call.details.handle?.schemeSpecificPart)
        }
        startActivity(intent)
    }
}