package com.volkovdev.openmarket

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.FileOutputStream
import java.net.InetAddress
import java.net.Socket

class ProxyService : VpnService() {

    private var vpnThread: Thread? = null
    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        vpnThread = Thread {
            vpnInterface = establishVpn()
            val localIpAddress = InetAddress.getLocalHost().hostAddress
            val socket = Socket(localIpAddress, 6667)
            val input = socket.getInputStream()
            val output = FileOutputStream(vpnInterface!!.fileDescriptor)

            val buffer = ByteArray(1024)
            Log.d("Proxy service","Starting service")
            while (true) {
                val bytesRead = input.read(buffer)
                if (bytesRead == -1) break
                output.write(buffer, 0, bytesRead)
                output.flush()
            }
        }
        vpnThread?.start()

        return START_STICKY
    }

    override fun onDestroy() {
        vpnThread?.interrupt()
        vpnInterface?.close()
        super.onDestroy()
    }

    private fun establishVpn(): ParcelFileDescriptor? {
        val builder = Builder()
        builder.addAddress("10.0.0.2", 32)
        builder.addRoute("0.0.0.0", 0)
        builder.setSession("MyVPNService")
        Log.d("Proxy service","Establishing vpn")
        return builder.establish()
    }
}