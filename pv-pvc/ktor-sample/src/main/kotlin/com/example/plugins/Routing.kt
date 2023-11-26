package com.example.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    val filesPath = "/data"

    routing {
        get("/health") {
            call.respond(HttpStatusCode.OK)
        }
        get("/download/{name}") {
            val filename = call.parameters["name"]!!
            val file = File("$filesPath/$filename")
            if (file.exists()) {
                call.respondFile(file)
            } else call.respond(HttpStatusCode.NotFound)
        }
        post("/upload") {
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val name = part.originalFileName!!
                    val file = File("$filesPath/$name")
                    part.streamProvider().use { its ->
                        file.outputStream().buffered().use { its.copyTo(it) }
                    }
                }
                part.dispose()
            }
        }
    }
}
