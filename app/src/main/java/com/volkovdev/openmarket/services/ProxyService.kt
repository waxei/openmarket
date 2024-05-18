package com.volkovdev.openmarket.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.volkovdev.openmarket.server.ProxyServer

class ProxyService : Service() {
    private lateinit var proxyServer: ProxyServer

    override fun onCreate() {
        super.onCreate()

        // Create the proxy server
        proxyServer = ProxyServer(8080)
        Log.d("ProxyService", "example created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("SERVICE","ONSTARTCOMMAND")
        // Start the proxy server in a separate thread
        Thread {
            proxyServer.start()
        }.start()
        val ip = proxyServer.getIp()

        Log.d("ProxyService", "IP address: $ip")

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        // Stop the proxy server
        proxyServer.stop()
        Log.d("ProxyService", "Server shut down successfully")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}