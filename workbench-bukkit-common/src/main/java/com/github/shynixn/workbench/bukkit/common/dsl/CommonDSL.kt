package com.github.shynixn.workbench.bukkit.common.dsl

import com.github.shynixn.workbench.bukkit.common.implementation.LoggerImpl
import com.github.shynixn.workbench.bukkit.common.implementation.WorkBenchResourceImpl
import com.github.shynixn.workbench.minecraft.common.dsl.Position
import com.github.shynixn.workbench.minecraft.common.dsl.position
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector

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

/**
 * WorkBenchResource, call enable or disable on it.
 */
val workbenchResource: WorkbenchResource =
    WorkBenchResourceImpl()

/**
 * Logs a message.
 */
fun log(f: Logger.() -> Unit) {
    val plugin = (workbenchResource as WorkBenchResourceImpl).plugin!!
    val logger = LoggerImpl()
    f.invoke(logger)
    logger.log(plugin.logger)
}

/**
 * Gets the player by the name.
 */
fun player(f: () -> String): Player {
    return Bukkit.getPlayer(f.invoke())!!
}

/**
 * Gets the current server version.
 */
val serverVersion: Version
    get() {
        return (workbenchResource as WorkBenchResourceImpl).serverVersion
    }

/**
 * Finds an NMS class from the current version.
 */
fun findClazz(classPath: String): Class<*> {
    return Class.forName(classPath.replace("VERSION", serverVersion.bukkitId))
}

/**
 * Translates the given chatColor.
 */
fun String.translateChatColors(): String {
    return ChatColor.translateChatColorCodes('&', this)
}

/**
 * Strips the given chatColor.
 */
fun String.stripChatColors(): String {
    return ChatColor.stripChatColors(this)
}

/**
 * Color.
 */
fun red(): String {
    return ChatColor.RED.toString()
}

/**
 * Creates a new location.
 */
fun location(f: Position.() -> Unit): Location {
    val position = position(f)
    return position.toLocation()
}

/**
 * Creates a new vector.
 */
fun vector(f: Position.() -> Unit): Vector {
    val position = position(f)
    return position.toVector()
}

/**
 * Creates a new eulerAngle.
 */
fun eulerAngle(f: Position.() -> Unit): EulerAngle {
    val position = position(f)
    return position.toEulerAngle()
}

/**
 * Converts the location to a position.
 */
fun Location.toPosition(): Position {
    val outer = this
    return position {
        worldName = outer.world!!.name
        x = outer.x
        y = outer.y
        z = outer.z
        yaw = outer.yaw.toDouble()
        pitch = outer.pitch.toDouble()
    }
}

/**
 * Converts the position to a vector.
 */
fun Position.toVector(): Vector {
    return Vector(this.x, this.y, this.z)
}

/**
 * Converts the position to an eulerAngle.
 */
fun Position.toEulerAngle(): EulerAngle {
    return EulerAngle(this.x, this.y, this.z)
}

/**
 * Converts the position to a location.
 * @param worldName Optional world description.
 */
fun Position.toLocation(worldName: String? = null): Location {
    val world = if (worldName != null) {
        Bukkit.getWorld(worldName)!!
    } else {
        Bukkit.getWorld(this.worldName!!)!!
    }

    return Location(world, this.x, this.y, this.z, this.yaw.toFloat(), this.pitch.toFloat())
}

/**
 * Gets the position of the entity.
 */
val Entity.position: Position
    get() {
        return this.location.toPosition()
    }


