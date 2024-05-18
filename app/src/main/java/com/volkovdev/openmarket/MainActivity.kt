package com.volkovdev.openmarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.volkovdev.openmarket.services.ProxyService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent = Intent(this, ProxyService::class.java)
        val button:Button = findViewById(R.id.searchButton)
        button.setOnClickListener {
            Log.d("Main","Trying to start...")
            startService(intent)
            val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibratorService.vibrate(200)

        }
        val stop:Button = findViewById(R.id.stopProxy)
        stop.setOnClickListener{
            stopService(intent)
            Log.d("Main","Service stopped")
        }


    }

}