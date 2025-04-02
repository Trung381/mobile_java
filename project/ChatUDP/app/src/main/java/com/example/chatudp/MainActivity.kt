package com.example.chatudp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity() {
    private lateinit var ipEditText: EditText
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var receivedTextView: TextView
    private val RECEIVE_PORT = 12345 // Cổng nhận tin nhắn
    private val SEND_PORT = 12345 // Cổng gửi tin nhắn
    private var isReceiving = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo views
        ipEditText = findViewById(R.id.ipEditText)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)
        receivedTextView = findViewById(R.id.receivedTextView)

        // Bắt đầu nhận tin nhắn
        startReceiving()

        // Xử lý sự kiện nút Gửi
        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val ip = ipEditText.text.toString()
        val message = messageEditText.text.toString()

        if (ip.isEmpty() || message.isEmpty()) {
            receivedTextView.text = "Vui lòng nhập IP và tin nhắn"
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = DatagramSocket()
                val address = InetAddress.getByName(ip)
                val buffer = message.toByteArray()
                val packet = DatagramPacket(buffer, buffer.size, address, SEND_PORT)

                socket.send(packet)
                socket.close()

                withContext(Dispatchers.Main) {
                    messageEditText.text.clear()
                    receivedTextView.text = "Đã gửi: $message"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    receivedTextView.text = "Lỗi gửi: ${e.message}"
                }
            }
        }
    }

    private fun startReceiving() {
        if (isReceiving) return
        isReceiving = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = DatagramSocket(RECEIVE_PORT) // Sửa lỗi từ RE Receive_PORT thành RECEIVE_PORT
                val buffer = ByteArray(1024)

                while (isReceiving) {
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket.receive(packet)

                    val receivedMessage = String(packet.data, 0, packet.length)
                    val senderIp = packet.address.hostAddress

                    withContext(Dispatchers.Main) {
                        receivedTextView.text = "Nhận từ $senderIp: $receivedMessage"
                    }
                }
                socket.close()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    receivedTextView.text = "Lỗi nhận: ${e.message}"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isReceiving = false // Dừng nhận tin nhắn khi activity bị hủy
    }
}