package com.example.clientserver

import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var responseTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cho phép mạng trên main thread (chỉ để demo, thực tế nên dùng AsyncTask hoặc Coroutines)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        // Khởi tạo views
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)
        responseTextView = findViewById(R.id.responseTextView)

        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val message = messageEditText.text.toString()
        if (message.isEmpty()) return

        try {
            // Thay "192.168.1.x" bằng địa chỉ IP của server (PC của bạn)
            val socket = Socket("192.168.1.5", 12345)
            val output = PrintWriter(socket.getOutputStream(), true)
            val input = BufferedReader(InputStreamReader(socket.getInputStream()))

            // Gửi tin nhắn
            output.println(message)

            // Nhận phản hồi
            val response = input.readLine()
            runOnUiThread {
                responseTextView.text = response
            }

            // Đóng kết nối
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
            runOnUiThread {
                responseTextView.text = "Lỗi: ${e.message}"
            }
        }
    }
}