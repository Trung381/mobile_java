package com.example.timecounting

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.timecounting.ui.theme.TimeCountingTheme

class MainActivity : ComponentActivity() {
    private lateinit var tvTimer: TextView
    private lateinit var btnStartStop: Button
    private var seconds = 0
    private var isRunning = false
    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                seconds++
                tvTimer.text = "$seconds giây"
                handler.postDelayed(this, 1000) // Cập nhật mỗi giây
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimer = findViewById(R.id.tvTimer)
        btnStartStop = findViewById(R.id.btnStartStop)

        btnStartStop.setOnClickListener {
            if (isRunning) {
                stopTimer()
            } else {
                startTimer()
            }
        }
    }

    private fun startTimer() {
        isRunning = true
        handler.post(runnable) // Bắt đầu cập nhật
        btnStartStop.text = "Dừng"
    }

    private fun stopTimer() {
        isRunning = false
        handler.removeCallbacks(runnable) // Dừng cập nhật
        btnStartStop.text = "Bắt đầu"
    }
}