package com.github.shynixn.workbench.bukkit.testsuite.arena

import com.github.shynixn.workbench.bukkit.async.dsl.launch
import com.github.shynixn.workbench.bukkit.common.dsl.ChatColor
import com.github.shynixn.workbench.bukkit.common.dsl.CommandExecutor
import com.github.shynixn.workbench.bukkit.yaml.dsl.get
import org.bukkit.entity.Player

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
class FateCommandExecutor(private val kitService: KitServiceImpl = KitServiceImpl()) : CommandExecutor {
    /**
     * Gets called when the given [source] executes the defined command with the given [args].
     */
    override fun <S> onExecuteCommand(source: S, args: Array<out String>) {
        launch {

            onExecutePlayer(source, args)
        }
    }

    /**
     * Gets called when the given [source] executes the defined command with the given [args].
     */
    private suspend fun <S> onExecutePlayer(source: S, args: Array<out String>) {
        if (source !is Player) {
            return
        }

        source.sendMessage(ChatColor.RED.toString() + "???")


        if (args.size > 1 && args[0] == "kit") {
            val kitName = args[1]
            val kit = get<List<Kit>>().firstOrNull { e -> e.kitType.name.toLowerCase() == kitName.toLowerCase() }

            if (kit == null) {
                source.sendMessage(ChatColor.RED.toString() + "Kit '$kitName' does not exist.")
                return
            }
            kitService.selectKit(source, kit)
        }
    }
}