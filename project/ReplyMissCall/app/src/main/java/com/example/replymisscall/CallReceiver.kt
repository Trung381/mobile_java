package com.example.replymisscall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val bundle: Bundle? = intent.extras
            val state = bundle?.getString(TelephonyManager.EXTRA_STATE)
            val phoneNumber = bundle?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                Toast.makeText(context, "Cuộc gọi đến từ: $phoneNumber", Toast.LENGTH_SHORT).show()
            }

            // Nếu cuộc gọi chuyển sang trạng thái IDLE (nghĩa là đã kết thúc hoặc bị nhỡ)
            if (state == TelephonyManager.EXTRA_STATE_IDLE && phoneNumber != null) {
                sendAutoReplySMS(context, phoneNumber)
            }
        }
    }

    private fun sendAutoReplySMS(context: Context, phoneNumber: String) {
        try {
            val smsManager = SmsManager.getDefault()
            val message = "Xin chào! Hiện tại tôi đang bận. Tôi sẽ gọi lại bạn sau!"
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)

            Toast.makeText(context, "Đã gửi tin nhắn tự động đến $phoneNumber", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Gửi tin nhắn thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
