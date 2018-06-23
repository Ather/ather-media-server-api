package com.atherapp.athermediaserver.modules

import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext

typealias RouteHandler = (it: RoutingContext) -> Unit

interface ApiExtension {
    val endpoint: String

    val permissionNodes: Array<String>?

    val methods: Array<HttpMethod>

    val handler: RouteHandler
}

interface ApiExtensionHandler {
    fun handle(it: RoutingContext)
}

abstract class BaseApiExtension(
        override val endpoint: String,
        override val methods: Array<HttpMethod>,
        override val permissionNodes: Array<String>? = null,
        override val handler: RouteHandler
) : ApiExtension {
    constructor(
            endpoint: String,
            method: HttpMethod,
            permissionNode: String? = null,
            handler: RouteHandler
    ) : this(
            endpoint = endpoint,
            methods = arrayOf(method),
            permissionNodes = if (permissionNode != null) arrayOf(permissionNode) else null,
            handler = handler
    )
}

class TestApiExtension : BaseApiExtension(
        endpoint = "/api/modules/test",
        methods = arrayOf(HttpMethod.GET),
        permissionNodes = arrayOf("com.atherapp.test.permission"),
        handler = ::handle
) {
    companion object {
        private fun handle(it: RoutingContext) {
            it.next()
        }
    }
}

class TestApiExtension2 : BaseApiExtension(
        endpoint = "/api/modules/test",
        method = HttpMethod.GET,
        permissionNode = "com.atherapp.test.permission",
        handler = ::handle
) {
    companion object : ApiExtensionHandler {
        override fun handle(it: RoutingContext) {
            it.next()
        }
    }
}