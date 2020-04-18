package com.github.shynixn.workbench.bukkit.testsuite.angel

import com.github.shynixn.workbench.bukkit.common.log
import net.minecraft.server.v1_14_R1.*
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer

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
class WeepingAngelNMSPathfinder(
    private val weepingAngelZombie: WeepingAngelZombie,
    private val weepingAngelPathfinder: WeepingAngelPathfinder
) : PathfinderGoal() {
    private var path: PathEntity? = null
    private var isVisible = true
    private var navigationAppend = 2

    /**
     * Should goal be executed.
     */
    override fun a(): Boolean {
        weepingAngelZombie.fireTicks = 0

        try {
            weepingAngelPathfinder.flickerLightsAndBreakThroughDoors()
            val canMove = weepingAngelPathfinder.checkIfAngelIsAbleToMove()

            if (!canMove) {
                if (!isVisible) {
                //    sendPacket(PacketPlayOutSpawnEntityLiving(weepingAngelZombie))
                    weepingAngelPathfinder.playAppearingSound()
                    isVisible = true
                }

                this.path = this.weepingAngelZombie.navigation.a(
                    BlockPosition(
                        this.weepingAngelZombie.lastX,
                        this.weepingAngelZombie.lastY,
                        this.weepingAngelZombie.lastZ
                    ),
                    navigationAppend
                )
                this.c()
                return true
            }

            if (isVisible) {
          //      sendPacket(PacketPlayOutEntityDestroy(weepingAngelZombie.id))
                isVisible = false
            }

            val target = weepingAngelPathfinder.getNearestTarget() ?: return true

            if (target.location.distance(weepingAngelZombie.bukkitEntity.location) < 1.0) {
                weepingAngelPathfinder.onCatchPlayer(target)
            }

            val targetLocation = target.location
            this.path = this.weepingAngelZombie.navigation.a(
                BlockPosition(
                    targetLocation.x,
                    targetLocation.y,
                    targetLocation.z
                ), navigationAppend
            )
            this.c()
        } catch (e: Exception) {
            log {
                error { "Error while executing pathfinder." }
                throwable { e }
            }
        }
        return path != null
    }

    /**
     * OnStartExecuting.
     */
    override fun c() {
        this.weepingAngelZombie.navigation.a(this.path, 2.5)
    }

    /**
     * Sends a packet to all world players.
     */
    private fun sendPacket(packet: Packet<*>) {
        for (player in weepingAngelZombie.bukkitEntity.location.world!!.players) {
            (player as CraftPlayer).handle.playerConnection.sendPacket(packet)
        }
    }
}