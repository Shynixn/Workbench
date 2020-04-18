@file:Suppress("UNCHECKED_CAST")

package com.github.shynixn.workbench.bukkit.common

import com.github.shynixn.workbench.bukkit.entity.GenericAttribute
import com.github.shynixn.workbench.bukkit.entity.GenericAttributeType
import com.github.shynixn.workbench.bukkit.entity.GenericAttributeImpl
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.EulerAngle
import org.bukkit.util.Vector
import java.util.*
import kotlin.reflect.KProperty

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
    try {
        val plugin = (workbenchResource as WorkBenchResourceImpl).plugin!!
        val logger = LoggerImpl()
        f.invoke(logger)
        logger.log(plugin.logger)
    } catch (e: Exception) {
        val logger = LoggerImpl()
        f.invoke(logger)
        logger.log(java.util.logging.Logger.getAnonymousLogger())
    }
}

/**
 * Gets the player by the name.
 */
fun player(f: () -> String): Player {
    return Bukkit.getPlayer(f.invoke())!!
}

/**
 * Clears all entities on the server.
 */
fun clearAllEntities() {
    Bukkit.getServer().worlds.stream().forEach { world ->
        world.entities.forEach { e ->
            if (e !is Player) {
                e.remove()
            }
        }
    }
}

/**
 * Spawns a new entity.
 */
fun <T> entity(f: () -> Pair<EntityType, Location>): T {
    val entityPair = f.invoke()
    return entityPair.second.world!!.spawnEntity(entityPair.second, entityPair.first) as T
}

/**
 * Applies a generic attribute to the living entity.
 */
fun LivingEntity.genericAttribute(f: GenericAttribute.() -> Unit) {
    val genericAttribute =
        GenericAttributeImpl(
            GenericAttributeType.FOLLOW_RANGE,
            0.0
        )
    f.invoke(genericAttribute)
}

/**
 * Gets the player by the uuid.
 */
fun playerByUUID(f: () -> String): Player {
    return Bukkit.getPlayer(UUID.fromString(f.invoke()))!!
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
 * Registers a preconfigured command.
 */
fun PluginManager.registerCommands(command: String, commandExecutor: CommandExecutor, plugin: Plugin) {
    require(plugin is JavaPlugin)
    plugin.getCommand(command)!!.setExecutor(
        CommandExecutorImpl(
            commandExecutor
        )
    )
}

/**
 * Translates the given chatColor.
 */
fun String.translateChatColors(): String {
    return ChatColor.translateChatColorCodes(
        '&',
        this
    )
}

/**
 * Strips the given chatColor.
 */
fun String.stripChatColors(): String {
    return ChatColor.stripChatColors(this)
}


/**
 * Creates a new position.
 */
fun position(f: Position.() -> Unit): Position {
    val position = PositionImpl()
    f.invoke(position)
    return position
}

/**
 * Compares if this position and the other position are the same in the given properties.
 */
fun Position.matches(position: Position, vararg properties: KProperty<*>): Boolean {
    for (property in properties) {
        if (property == Position.x && this.x != position.x) {
            return false
        } else if (property == Position.y && this.y != position.y) {
            return false
        } else if (property == Position.z && this.z != position.z) {
            return false
        } else if (property == Position.yaw && this.yaw != position.yaw) {
            return false
        } else if (property == Position.pitch && this.pitch != position.pitch) {
            return false
        } else if (property == Position.worldName && this.worldName != position.worldName) {
            return false
        } else if (property == Position.blockX && this.blockX != position.blockX) {
            return false
        } else if (property == Position.blockY && this.blockY != position.blockY) {
            return false
        } else if (property == Position.blockZ && this.blockZ != position.blockZ) {
            return false
        }
    }

    return true
}

/**
 * Compares if this position and the other position are the same without the given properties.
 */
fun Position.matchesExcept(position: Position, vararg properties: KProperty<*>): Boolean {
    return matches(position, *Position.all.asSequence().filter { e -> !properties.contains(e) }.toList().toTypedArray())
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


