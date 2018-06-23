package com.atherapp.athermediaserver.api

import com.atherapp.athermediaserver.modules.apiModules
import com.atherapp.common.modules.DefaultModuleManager
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import java.lang.management.ManagementFactory

/**
 * How long in milliseconds that the JVM has been running
 */
val uptimeMillis: Long
    get() = ManagementFactory.getRuntimeMXBean().uptime

/**
 * How long in decimal seconds that the JVM has been running
 */
val uptimeSeconds: Double
    get() = uptimeMillis / 1000.0

/**
 * Primary API controller inside the application
 * TODO Add options inside configurations, as in Port, IP, etc.
 * TODO Add SSL cert generation, and automatically enable it. Also allow custom SSL options to be specified
 */
object Api {
    /**
     * Default [Vertx] instance used to handle the API
     */
    lateinit var vertx: Vertx
        internal set

    /**
     * [HttpServer] used by the main [Vertx] instance
     */
    lateinit var server: HttpServer
        internal set

    /**
     * Root [Router], used when setting a custom HTTP root on the server
     */
    private lateinit var rootRouter: Router

    /**
     * [Router] used to bind to the memes
     */
    lateinit var router: Router
        internal set

    /**
     * [Router] used for binding endpoints to "/api*"
     */
    lateinit var apiRouter: Router
        internal set

    init {
        start()
    }

    internal fun start() {
        // Initialize Vert.x components
        vertx = Vertx.vertx()!!
        server = vertx.createHttpServer()!!
        rootRouter = Router.router(vertx)!!
        router = Router.router(vertx)!! // if (::router.isInitialized) router else Router.router(vertx)!!
        apiRouter = Router.router(vertx)!! // if (::router.isInitialized) apiRouter else Router.router(vertx)!!

        // TODO Use HTTP root from config
        // Mount the standard router to the root
        rootRouter.mountSubRouter("/", router)

        // Mount the default /api endpoint to the router
        router.mountSubRouter("/api", apiRouter)

        // API Component Initialization
        for (apiModule in DefaultModuleManager.apiModules()) {
            // This is the only special case we need to handle, see below for details
            if (apiModule.mountRouter != null && !apiModule.mountPoint.isNullOrBlank())
                apiModule.mountRouter?.mountSubRouter(apiModule.mountPoint, apiModule.router)
            // All other API Modules should be mounting to either the default router or a custom router already mounted to the default router.
        }

        // Pass server requests into the router
        // TODO get bind port from config, use HttpServerOptions instead of passing to listen
        server.requestHandler { router.accept(it) }.listen(30120) {
            if (it.succeeded())
                println("API Startup Completed: $uptimeSeconds seconds")
            // TODO Create a Module initialization stage here through PF4J (ex: OnApiReady, OnStart, etc.)
            else {
            }
        }
    }

    /**
     * Consider a more friendly approach to allow re-initialization
     */
    internal fun stop() {
        apiRouter.clear()
        router.clear()
        rootRouter.clear()
        server.close()
        vertx.close()
    }
}