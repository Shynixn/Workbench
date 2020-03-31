package com.github.shynixn.workbench.async.implementation

import com.github.shynixn.workbench.common.dsl.WorkbenchResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import org.bukkit.plugin.Plugin

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
class AsyncWorkbenchResource : WorkbenchResource {
    var registered: Boolean = false
    var asyncDispatcher: AsyncCoroutineDispatcher? = null
    var syncDispatcher: SyncCoroutineDispatcher? = null

    /**
     * Registers a new sub workBench resource. Gets immediately enabled if the parent
     * workBench resource is enabled. Gets automatically disabled if the parent is disabled.
     */
    override fun registerSubResource(workbenchResource: WorkbenchResource) {
    }

    /**
     * Allocates all workBench resources.
     */
    override fun onEnable(plugin: Plugin) {
        if (asyncDispatcher != null) {
            throw IllegalArgumentException("AsyncWorkBench cannot be enabled twice!")
        }

        asyncDispatcher = AsyncCoroutineDispatcher(plugin)
        syncDispatcher = SyncCoroutineDispatcher(plugin)
    }

    /**
     * Frees all workBench resources.
     */
    override fun onDisable() {
        if (asyncDispatcher == null) {
            return
        }

        asyncDispatcher!!.cancel()
        asyncDispatcher!!.cancelChildren()
        syncDispatcher!!.cancel()
        syncDispatcher!!.cancelChildren()
        asyncDispatcher = null
        syncDispatcher = null
    }
}