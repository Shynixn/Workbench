package com.github.shynixn.workbench.bukkit.testsuite.angel

import com.github.shynixn.workbench.bukkit.async.launch
import com.github.shynixn.workbench.bukkit.common.findClazz
import com.github.shynixn.workbench.bukkit.common.location
import com.github.shynixn.workbench.bukkit.sound.sound
import com.github.shynixn.workbench.bukkit.item.toMaterial
import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap


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
class WeepingAngelPathfinder(private val weepingAngel: WeepingAngel) {
    private val cachedBlocks = HashMap<Location, Pair<Material, Int>>()
    private val random = Random()
    private val setDataMethod = findClazz("org.bukkit.craftbukkit.VERSION.block.CraftBlock")
        .getDeclaredMethod("setData", Byte::class.java)

    /**
     * Checks if the angel is able to move freely or stuck in place.
     */
    fun checkIfAngelIsAbleToMove(): Boolean {
        return weepingAngel.location.world!!.players.asSequence()
            .filter { p -> isPlayerLookingAtEntity(p) && p.hasLineOfSight(weepingAngel.handle) }.count() == 0
    }

    fun playAppearingSound() {
        val location = weepingAngel.location

        sound {
            name = "ENDERMAN_TELEPORT,ENTITY_ENDERMAN_TELEPORT"
        }.play(location, location.world!!.players)
    }

    fun onCatchPlayer(player: Player) {

    }

    fun flickerLightsAndBreakThroughDoors() {
        launch {
            val angelLocation = weepingAngel.location

            val locations  =
                (-10 until 10).zip(-10 until 10).zip(-10 until 10).asSequence().map { applier ->
                    location {
                        worldName = angelLocation.world!!.name
                        x = angelLocation.x + applier.first.first
                        y = angelLocation.x + applier.first.second
                        z = angelLocation.x + applier.second
                    }
                }.filter { l ->
                    l.block.type == Material.GLOWSTONE || l.block.type == Material.TORCH || l.block.type == "REDSTONE_TORCH_ON".toMaterial()
                            || l.block.type == "REDSTONE_LAMP_ON".toMaterial() || l.block.type == "WOOD_DOOR".toMaterial()
                }.filter { random.nextInt(100) < 10 }.toList()

            for (location in locations) {
                cachedBlocks[location] = Pair(location.block.type, location.block.data.toInt())
                val blockType = location.block.type

                if (blockType == Material.GLOWSTONE || blockType == "REDSTONE_LAMP_ON".toMaterial()) {
                    location.block.type = "REDSTONE_LAMP_OFF".toMaterial()
                } else if (blockType == Material.TORCH || blockType == "REDSTONE_TORCH_ON".toMaterial()) {
                    location.block.type = "REDSTONE_TORCH_OFF".toMaterial()
                } else if (blockType == "WOOD_DOOR".toMaterial()) {
                    location.block.type = Material.AIR
                    playDoorBreakSound(location)
                }
            }

            delay(2000)

            for (location in locations) {
                if (cachedBlocks.containsKey(location)) {
                    continue
                }

                val cachedBlock = cachedBlocks[location]!!
                location.block.type = cachedBlock.first
                setDataMethod.invoke(location.block, cachedBlock.second.toByte())

                cachedBlocks.remove(location)
            }
        }
    }

    /**
     * Gets the nerares player target.
     */
    fun getNearestTarget(): Player? {
        val distance = 1000.0 * 1000.0

        return weepingAngel.location.world!!.players.asSequence()
            .map { p -> Pair(p, p.location.distanceSquared(weepingAngel.location)) }
            .filter { e -> e.second < distance }
            .minBy { e -> e.second }?.first
    }

    private fun playDoorBreakSound(location: Location) {
        sound {
            name = "ZOMBIE_WOODBREAK"
        }.play(location, location.world!!.players)
    }

    private fun isPlayerLookingAtEntity(player: Player): Boolean {
        val pos = player.location.toVector().subtract(weepingAngel.location.toVector()).normalize()
        val dot = pos.dot(player.location.direction)
        return dot < -0.5
    }
}