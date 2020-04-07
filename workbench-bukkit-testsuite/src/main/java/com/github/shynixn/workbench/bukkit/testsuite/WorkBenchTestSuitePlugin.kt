package com.github.shynixn.workbench.bukkit.testsuite

import com.github.shynixn.workbench.bukkit.async.dsl.launch
import com.github.shynixn.workbench.bukkit.common.dsl.ChatColor
import com.github.shynixn.workbench.bukkit.common.dsl.player
import com.github.shynixn.workbench.bukkit.common.dsl.workbenchResource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import org.bukkit.plugin.java.JavaPlugin

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
class WorkBenchTestSuitePlugin : JavaPlugin() {
    private val prefix = ChatColor.GREEN.toString() + "[TestSuite] " + ChatColor.WHITE

    /**
     * OnEnable.
     */
    override fun onEnable()  {
        workbenchResource.onEnable(this)
        val player = player { "Shynixn" }

        launch {
            player.sendMessage(prefix + "Loading [Particle] testsuite....")
            for(i in 0 until 10){
                player.sendMessage("Playing [Particle] testsuite ${i+1}")
                delay(1500)
                val particleTestSuite = ParticleTestSuite()
                particleTestSuite.play(player)
            }

            player.sendMessage(prefix + "Completed [Particle] testsuite.")
            player.sendMessage(prefix + "Completed testsuite.")
        }
    }

    override fun onDisable() {
        workbenchResource.onDisable()
    }
}