package com.github.shynixn.workbench.bukkit.particle.implementation

import com.github.shynixn.workbench.bukkit.async.dsl.async
import com.github.shynixn.workbench.bukkit.async.dsl.launchAsync
import com.github.shynixn.workbench.bukkit.common.dsl.log
import com.github.shynixn.workbench.bukkit.item.dsl.item
import com.github.shynixn.workbench.bukkit.item.dsl.toItemStack
import com.github.shynixn.workbench.bukkit.item.dsl.toMaterial
import com.github.shynixn.workbench.bukkit.particle.dsl.Particle
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

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
class ParticleImpl : Particle {
    /**
     * Gets or sets the particle typeName.
     */
    override var name: String = ""

    /**
     * Amount of particles.
     */
    override var amount: Int = 1
    /**
     * Particle speed.
     */
    override var speed: Double = 0.0

    /**
     * Offset for the x coordinate.
     */
    override var offSetX: Double = 0.0

    /**
     * Offset for the y coordinate.
     */
    override var offSetY: Double = 0.0

    /**
     * Offset for the z coordinate.
     */
    override var offSetZ: Double = 0.0
    /**
     * Material value.
     */
    override var materialName: String? = null

    /**
     * Data value.
     */
    override var dataValue: Int = 0

    /**
     * RGB Color code of red.
     */
    override var colorRed: Int
        get() = this.offSetX.toInt()
        set(value) {
            this.offSetX = value.toDouble()
        }

    /**
     * RGB Color code of green.
     */
    override var colorGreen: Int
        get() = this.offSetY.toInt()
        set(value) {
            this.offSetY = value.toDouble()
        }
    /**
     * RGB Color code of blue.
     */
    override var colorBlue: Int
        get() = this.offSetZ.toInt()
        set(value) {
            this.offSetZ = value.toDouble()
        }

    /**
     * Plays the particle.
     */
    override fun play(location: Location, vararg players: Player) {
        launchAsync {
            play(location, players.asList())
        }
    }

    /**
     * Plays the particle.
     */
    override fun play(location: Location, players: Collection<Player>) {
        launchAsync {
            playInternalParticle(location, players)
        }
    }

    /**
     * Play internal particle.
     */
    private suspend fun playInternalParticle(location: Location, players: Collection<Player>) {
        if (name == "" || name.equals("none", true)) {
            return
        }

        val bukkitParticleType =
            org.bukkit.Particle.values().asSequence().firstOrNull { p -> p.name.equals(name, true) || name == p.name }

        if (bukkitParticleType == null) {
            log {
                warning { "Failed to parse particle '$name'." }
            }
            return
        }

        try {
            for (player in players) {
                playParticleForPlayer(bukkitParticleType, location, player)
            }
        } catch (e: Exception) {
            log {
                warning { "Failed to play particle '$name'." }
                throwable { e }
            }
        }
    }

    /**
     * Plays a single particle.
     */
    private suspend fun playParticleForPlayer(
        bukkitParticleType: org.bukkit.Particle,
        location: Location,
        player: Player
    ) {
        val dataType = bukkitParticleType.dataType

        when (dataType) {
            Void::class.java -> player.spawnParticle(
                bukkitParticleType,
                location,
                amount,
                offSetX,
                offSetY,
                offSetZ,
                speed
            )
            org.bukkit.Particle.DustOptions::class.java -> {
                val dustOptions =
                    org.bukkit.Particle.DustOptions(
                        org.bukkit.Color.fromRGB(
                            colorRed,
                            colorGreen,
                            colorBlue
                        ), 1.0F
                    )
                player.spawnParticle(bukkitParticleType, location, 0, dustOptions)
            }
            MaterialData::class.java -> {
                val itemType = materialName!!.toMaterial()
                val materialData = MaterialData(itemType, dataValue.toByte())
                player.spawnParticle(
                    bukkitParticleType,
                    location,
                    amount,
                    offSetX,
                    offSetY,
                    offSetZ,
                    speed,
                    materialData
                )
            }
            BlockData::class.java -> {
                val itemType = materialName!!.toMaterial()
                val blockData = Bukkit.createBlockData(itemType)

                player.spawnParticle(
                    bukkitParticleType,
                    location,
                    amount,
                    offSetX,
                    offSetY,
                    offSetZ,
                    speed,
                    blockData
                )
            }
            ItemStack::class.java -> {
                val outer = this
                val itemStack = item {
                    name = outer.materialName!!
                    dataValue = outer.dataValue
                }.toItemStack()

                player.spawnParticle(
                    bukkitParticleType,
                    location,
                    amount,
                    offSetX,
                    offSetY,
                    offSetZ,
                    speed,
                    itemStack
                )
            }
            else -> {
                throw IllegalArgumentException("Unknown particle!")
            }
        }
    }
}