package com.github.shynixn.workbench.bukkit.yaml.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.github.shynixn.workbench.bukkit.async.dsl.launchAsync
import com.github.shynixn.workbench.bukkit.common.dsl.WorkbenchResource
import org.bukkit.plugin.Plugin
import java.lang.reflect.Type

/**
 * Created by Shynixn 2020.
 * <p>
 * Version 1.5
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2020 by Shynixn
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
internal class YamlWorkbenchResource : WorkbenchResource {
    /**
     * RunTimeFileCache.
     */
    var fileCache = HashMap<Type, Resource<*>>()
    /**
     * Object Mapper.
     */
    var objectMapper: ObjectMapper =
        ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))

    /**
     * Is enabled.
     */
    var enabled: Boolean = false

    /**
     * Allocates all workBench resources.
     */
    override fun onEnable(plugin: Plugin) {
        if (enabled) {
            throw IllegalArgumentException("YamlWorkbench cannot be enabled twice!")
        }

        enabled = true
        objectMapper = ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
        plugin.server.scheduler.runTaskTimerAsynchronously(plugin, Runnable {
            launchAsync {
                for (cacheResource in fileCache.toList()) {
                    if (cacheResource.second.cache != null) {
                        cacheResource.second.onSave.invoke(cacheResource.second.cache!!)
                    }
                }
            }
        }, 1L, 20L * 5)
    }

    /**
     * Performs a lightweight reload on resources.
     */
    override fun reload() {
        fileCache.clear()
    }

    /**
     * Frees all workBench resources.
     */
    override fun onDisable() {
    }
}