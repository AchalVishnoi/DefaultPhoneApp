package com.example.defaultphoneapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.Manifest
import android.widget.ImageView
import androidx.core.app.ActivityCompat

class DialerActivity : Activity() {

    private lateinit var etPhoneNumber: EditText
    private lateinit var btnCall: Button
    private lateinit var telecomManager: TelecomManager
    private lateinit var phoneAccountHandle: PhoneAccountHandle
    private lateinit var rvRecentCalls: RecyclerView
    private lateinit var contactbtn: ImageView

    private val REQUEST_CODE_CALL_LOG = 1001


    @SuppressLint("NewApi", "MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialer)

        requestCallLogPermission()

        val num = intent.getStringExtra("phone_number") ?: ""


        etPhoneNumber = findViewById(R.id.et_phone_number)
        btnCall = findViewById(R.id.btn_call)

        etPhoneNumber.setText(num)

        rvRecentCalls = findViewById(R.id.rv_recent_calls)
        contactbtn=findViewById(R.id.contacts)

        rvRecentCalls.layoutManager = LinearLayoutManager(this)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CALL_LOG),
               REQUEST_CODE_CALL_LOG
            )
        } else {
           rvRecentCalls.adapter= CallLogsAdapter(getRecentCalls())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestDefaultDialerRole()
        }

        telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        phoneAccountHandle = PhoneAccountHandle(
            ComponentName(this, MyConnectionService::class.java),
            "MyPhoneAccount"
        )


        btnCall.setOnClickListener {
            val phoneNumber = etPhoneNumber.text.toString()
            if (phoneNumber.isNotEmpty()) {
                makeCall(phoneNumber)
                val intent = Intent(this, OngoingCallActivity::class.java)
                var name=getContactName(phoneNumber)?:phoneNumber
                intent.putExtra("CALLER_NUMBER", name)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
        }

        contactbtn.setOnClickListener{
            val intent = Intent(this, ContactsActivity::class.java)

            startActivity(intent)
        }

        registerPhoneAccount()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestDefaultDialerRole() {
        val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
        if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) && !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            startActivityForResult(intent, 1001)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            if (roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
                Log.d("Dialer", "App is now the default dialer")
            } else {
                Log.d("Dialer", "User denied making this the default dialer")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerPhoneAccount() {
        val phoneAccount = PhoneAccount.builder(phoneAccountHandle, "My Calling App")
            .setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED)
            .setHighlightColor(0xFF0000)
            .setShortDescription("Custom Dialer")
            .build()

        telecomManager.registerPhoneAccount(phoneAccount)
    }

    @SuppressLint("MissingPermission")
    private fun makeCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1002)
            return
        }

        val phoneAccountHandle = telecomManager.callCapablePhoneAccounts.firstOrNull()
        if (phoneAccountHandle == null) {
            Log.e("Dialer", "No registered phone accounts found.")
            return
        }

        val uri = Uri.fromParts("tel", phoneNumber, null)
        val extras = Bundle()
        extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle)

        telecomManager.placeCall(uri, extras)
    }



    private fun getRecentCalls(): List<CallLog> {
        val callLogs = mutableListOf<com.example.defaultphoneapp.CallLog>()

        // Query the call log
        val cursor: Cursor? = contentResolver.query(
            android.provider.CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            android.provider.CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(android.provider.CallLog.Calls.NUMBER)
            val typeIndex = it.getColumnIndex(android.provider.CallLog.Calls.TYPE)
            val dateIndex = it.getColumnIndex(android.provider.CallLog.Calls.DATE)
            val durationIndex = it.getColumnIndex(android.provider.CallLog.Calls.DURATION)
      var i=0;
            while (it.moveToNext()&&i<=50) {
                val number = it.getString(numberIndex)
                val type = when (it.getInt(typeIndex)) {
                    android.provider.CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    android.provider.CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    android.provider.CallLog.Calls.MISSED_TYPE -> "Missed"
                    else -> "Unknown"
                }
                val date = it.getLong(dateIndex)
                val duration = it.getString(durationIndex)

                // Format the date
                val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(Date(date))
                val name = getContactName(number) ?: "Unknown"

                // Add to the list
                callLogs.add(CallLog(name,number, type, formattedDate, duration))
                i++;
            }
        }

        return callLogs
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


    private fun requestCallLogPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_CALL_LOG),
                REQUEST_CODE_CALL_LOG)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CALL_LOG) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getRecentCalls()
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
