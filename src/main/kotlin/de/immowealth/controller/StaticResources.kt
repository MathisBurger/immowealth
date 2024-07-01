package de.immowealth.controller

import io.quarkus.runtime.StartupEvent
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import jakarta.enterprise.event.Observes
import java.io.File

/**
 * Handles static resources
 */
class StaticResources {

    fun installRoutes(@Observes startupEvent: StartupEvent, router: Router) {
        router.route()
            .path("/*")
            .handler(StaticHandler.create("static/"))
        router.errorHandler(404) { routingContext ->
            var file = File("static/" + routingContext.request().path() + ".html")
            if (!file.canRead()) {
                file = File("static/404.html");
            }
            routingContext.response().setStatusCode(200).end(file.readText())
        }
    }
}