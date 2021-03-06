package helper

import com.github.shynixn.workbench.bukkit.common.workbenchResource
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import org.mockito.Mockito
import java.util.concurrent.Executors
import java.util.logging.Logger

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
class MockServer {
    @Suppress("SENSELESS_COMPARISON")
    companion object {
        fun init(){
            val plugin = Mockito.mock(Plugin::class.java)
            val server = Mockito.mock(Server::class.java)
            val scheduler = Mockito.mock(BukkitScheduler::class.java)
            val asyncThreadPool = Executors.newFixedThreadPool(2)
            val syncThreadPool = Executors.newSingleThreadExecutor()

            Mockito.`when`(plugin.server).thenReturn(server)
            Mockito.`when`(plugin.isEnabled).thenReturn(true)
            Mockito.`when`(server.scheduler).thenReturn(scheduler)
            Mockito.`when`(
                scheduler.runTaskAsynchronously(
                    Mockito.any(Plugin::class.java),
                    Mockito.any(Runnable::class.java)
                )
            ).thenAnswer { e ->
                asyncThreadPool.submit(e.getArgument(1))
                Mockito.mock(BukkitTask::class.java)
            }
            Mockito.`when`(scheduler.runTask(Mockito.any(Plugin::class.java), Mockito.any(Runnable::class.java)))
                .thenAnswer { e ->
                    syncThreadPool.submit(e.getArgument(1))
                    Mockito.mock(BukkitTask::class.java)
                }
            Mockito.`when`(server.logger).thenReturn(Logger.getAnonymousLogger())
            Mockito.`when`(plugin.logger).thenReturn(Logger.getAnonymousLogger())

            syncThreadPool.submit(Runnable {
                val primaryThreadId = Thread.currentThread().id
                Mockito.`when`(server.isPrimaryThread).thenAnswer {
                    Thread.currentThread().id == primaryThreadId
                }
            }).get()

            if (Bukkit.getServer() == null) {
                Bukkit.setServer(server)
                workbenchResource.onEnable(plugin)
            }
        }
    }
}