package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var timerBinder: TimerService.TimerBinder
    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startButton = findViewById<Button>(R.id.startButton)
        val counter = 0

        val serviceIntent = Intent(this, TimerService::class.java)

        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                timerBinder = service as TimerService.TimerBinder
                isConnected = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isConnected = false
            }
        }

        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE)

        startButton.setOnClickListener {
            if (counter % 2 == 0)
                startButton.text = "Pause"
            else
                startButton.text = "Resume"
            startService(serviceIntent)

        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            unbindService(serviceConnection)
        }
    }
}