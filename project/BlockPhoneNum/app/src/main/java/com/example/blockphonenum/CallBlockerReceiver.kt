package com.example.blockphonenum

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast

class CallBlockerReceiver : BroadcastReceiver() {
    private val blockedNumbers = listOf("+84987654321", "+84912345678") // Danh sách số bị chặn

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                if (incomingNumber != null && blockedNumbers.contains(incomingNumber)) {
                    Toast.makeText(context, "Chặn cuộc gọi từ: $incomingNumber", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
