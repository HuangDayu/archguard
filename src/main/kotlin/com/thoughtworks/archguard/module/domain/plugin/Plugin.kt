package com.thoughtworks.archguard.module.domain.plugin

interface Plugin {
    fun getPluginType() : PluginType
}