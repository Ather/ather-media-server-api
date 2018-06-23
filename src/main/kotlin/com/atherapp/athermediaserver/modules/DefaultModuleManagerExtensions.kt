package com.atherapp.athermediaserver.modules

import com.atherapp.common.modules.DefaultModuleManager

/**
 * Get all [ApiModule] extensions in the [DefaultModuleManager]
 */
fun DefaultModuleManager.apiModules(): MutableList<ApiModule> = this.getExtensions(ApiModule::class.java)!!