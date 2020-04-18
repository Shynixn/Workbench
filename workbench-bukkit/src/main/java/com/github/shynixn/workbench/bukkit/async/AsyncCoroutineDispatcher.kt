package com.github.shynixn.workbench.bukkit.async

import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

/**
 * The SyncCoroutineDispatcher handles context switching to any async thread.
 */
class AsyncCoroutineDispatcher(private val plugin: Plugin) : CoroutineDispatcher() {
    /**
     *  Handles dispatching the coroutine on any async thread.
     *  @param context CoroutineContext to dispatch tasks.
     *  @param block runnable callback.
     */
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (!plugin.isEnabled) {
            return
        }

        if (Bukkit.isPrimaryThread()) {
            plugin.server.scheduler.runTaskAsynchronously(plugin, block)
        } else {
            block.run()
        }
    }
}