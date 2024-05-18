package com.volkovdev.openmarket.server

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

class ProxyServer(private val port: Int) {
    private lateinit var serverSocket: ServerSocket

    fun start() {
        serverSocket = ServerSocket(port)
        println("Proxy server started on port $port")
        while (true) {
            val clientSocket = serverSocket.accept()
            Log.d("ProxyServer", "Client connected")
            println("Client connected")
            Thread(ProxyHandler(clientSocket)).start()
        }
    }

    fun stop() {
        serverSocket.close()
    }
    fun getIp(): String? {
        return if (::serverSocket.isInitialized && serverSocket.isBound) {
            try {
                val en = serverSocket.localSocketAddress as InetSocketAddress
                en.address.hostAddress
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val proxyServer = ProxyServer(8080)
            proxyServer.start()
        }
    }
}

class ProxyHandler(private val clientSocket: Socket) : Runnable {
    override fun run() {
        try {
            val inputStream = clientSocket.getInputStream()
            val outputStream = clientSocket.getOutputStream()

            // Read the first line of the request, which contains the method, path and protocol version
            val requestLine = inputStream.bufferedReader().readLine()
            val tokens = requestLine.split(" ")
            val method = tokens[0]
            val path = tokens[1]
            val protocolVersion = tokens[2]

            // Extract the host and port from the request headers
            var host: String? = null
            var port: Int = 80
            var headerLine: String?
            do {
                headerLine = inputStream.bufferedReader().readLine()
                if (headerLine != null && headerLine.startsWith("Host: ")) {
                    host = headerLine.substring(6)
                    val tokens2 = host.split(":")
                    host = tokens2[0]
                    if (tokens2.size > 1) {
                        port = tokens2[1].toInt()
                    }
                }
            } while (!headerLine.isNullOrEmpty())

            // Connect to the remote server
            if (host != null) {
                val remoteAddr = InetSocketAddress(host, port)
                val remoteSocket = Socket()
                remoteSocket.connect(remoteAddr)

                // Send the request to the remote server
                remoteSocket.getOutputStream().write(requestLine.toByteArray())
                remoteSocket.getOutputStream().write("\r\n".toByteArray())
                for (header in inputStream.bufferedReader().lineSequence()) {
                    remoteSocket.getOutputStream().write(header.toByteArray())
                    remoteSocket.getOutputStream().write("\r\n".toByteArray())
                }
                remoteSocket.getOutputStream().write("\r\n".toByteArray())

                // Receive the response from the remote server and send it to the client
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (true) {
                    bytesRead = remoteSocket.getInputStream().read(buffer)
                    if (bytesRead <= 0) break
                    outputStream.write(buffer, 0, bytesRead)
                    outputStream.flush()
                }

                // Close the connections
                remoteSocket.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            clientSocket.close()
        }
    }
}