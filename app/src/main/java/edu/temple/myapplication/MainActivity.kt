package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var timerBinder: TimerService.TimerBinder
    private var isConnected = false
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startButton = findViewById<Button>(R.id.startButton)
        var counter = 0
        textView = findViewById(R.id.textView)

        val timerHandler = Handler(Looper.getMainLooper()) {
            textView.text = it.what.toString()
            true
        }

        val serviceIntent = Intent(this, TimerService::class.java)

        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                timerBinder = service as TimerService.TimerBinder
                timerBinder.setHandler(timerHandler)
                isConnected = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isConnected = false
            }
        }

        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE)

        startButton.setOnClickListener {
            if (isConnected) {
                if (counter % 2 == 0) {
                    startButton.text = "Pause"
                    timerBinder.start(100)
                    counter++

                } else {
                    startButton.text = "Resume"
                    timerBinder.pause()
                    counter++
                }
            }
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            unbindService(serviceConnection)
        }
    }
}