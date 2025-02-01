
package com.example.defaultphoneapp


import android.annotation.SuppressLint
import android.app.Activity
import android.app.role.RoleManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle

import android.telecom.TelecomManager
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi

class DialerActivity : Activity() {

    private lateinit var etPhoneNumber: EditText
    private lateinit var btnCall: Button

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialer)

        etPhoneNumber = findViewById(R.id.et_phone_number)
        btnCall = findViewById(R.id.btn_call)

        btnCall.setOnClickListener {
            val phoneNumber = etPhoneNumber.text.toString()
            if (phoneNumber.isNotEmpty()) {
                makeCall(phoneNumber)
            }
        }

        requestDefaultDialerRole()
    }

    @SuppressLint("MissingPermission")
    private fun makeCall(phoneNumber: String) {
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        val uri = Uri.fromParts("tel", phoneNumber, null)
        val extras = Bundle()
        telecomManager.placeCall(uri, extras)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestDefaultDialerRole() {
        val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
        if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER)) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER)
            startActivityForResult(intent, 1)
        }
    }
}