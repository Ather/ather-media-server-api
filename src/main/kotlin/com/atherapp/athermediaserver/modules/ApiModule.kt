package com.atherapp.athermediaserver.modules

import com.atherapp.athermediaserver.api.Api
import com.atherapp.common.modules.AtherExtensionPoint
import io.vertx.ext.web.Router

/**
 * [AtherExtensionPoint] used to create custom API Endpoints
 */
abstract class ApiModule(
        /**
         * @see ApiModule.mountRouter
         */
        mountRouter: Router? = null,
        /**
         * @see ApiModule.mountPoint
         */
        mountPoint: String? = null,
        /**
         * @see ApiModule.router
         */
        router: Router = Api.apiRouter
) : AtherExtensionPoint {
    /**
     * The [Router] to mount the [router] onto.
     * It is only used if it is not null, and [mountPoint] is not null
     *
     * @see <a href="https://vertx.io/docs/vertx-web/kotlin/#_sub_routers">Vert.x Documentation on Sub-routers</a>
     */
    open var mountRouter: Router? = mountRouter
        protected set

    /**
     * [String] representing the path under which to mount the [router] to the [mountRouter].
     * @see Router.mountSubRouter
     */
    open var mountPoint: String? = mountPoint
        protected set

    /**
     * The [Router] the endpoints in this module are attached to.
     * It is recommended to call this directly, but if required,
     * you could also use the [Api.router], or others, directly.
     *
     * @see <a href="https://vertx.io/docs/vertx-web/kotlin/#_basic_vert_x_web_concepts">Basic Vert.x-Web concepts</a>
     */
    open var router: Router = router
        protected set
}