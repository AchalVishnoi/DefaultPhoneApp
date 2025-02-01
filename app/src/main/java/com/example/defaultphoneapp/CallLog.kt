package com.example.defaultphoneapp

data class CallLog(
    val number: String, // Phone number
    val type: String,   // Call type (Incoming, Outgoing, Missed)
    val date: String,   // Call date/time
    val duration: String // Call duration
)