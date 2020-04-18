package com.github.shynixn.workbench.bukkit.common

interface CommandExecutor {
    /**
     * Gets called when the given [source] executes the defined command with the given [args].
     */
    fun <S> onExecuteCommand(source: S, args: Array<out String>)
}