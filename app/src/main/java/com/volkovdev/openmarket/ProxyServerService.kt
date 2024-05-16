package com.volkovdev.openmarket

import ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*


class ProxyServerService {

    fun main() {
        embeddedServer(Netty, port = 8080) {
            get("/") {
                val url = call.request.path().substring(1)
                val response = client.get<String>(url)
                call.respond(response)
            }
        }.start(wait = true)
    }

}