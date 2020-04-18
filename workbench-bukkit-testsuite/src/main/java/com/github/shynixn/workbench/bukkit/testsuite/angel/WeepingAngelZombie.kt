package com.github.shynixn.workbench.bukkit.testsuite.angel

import net.minecraft.server.v1_14_R1.*
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld
import org.bukkit.event.entity.CreatureSpawnEvent

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
class WeepingAngelZombie(weepingAngel: WeepingAngel) :
    EntityZombie(EntityTypes.ZOMBIE, (weepingAngel.location.world!! as CraftWorld).handle) {
    // BukkitEntity has to be self cached since 1.14.
    private var entityBukkit: Any? = null

    /**
     * Init.
     */
    init {
        this.isSilent = true
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).value = 150.0
        this.clearAIGoals()

        goalSelector.a(1, WeepingAngelNMSPathfinder(this, WeepingAngelPathfinder(weepingAngel)));

        val location = weepingAngel.location
        val mcWorld = (location.world as CraftWorld).handle
        this.setPosition(location.x, location.y + 1, location.z)
        mcWorld.addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM)
    }

    /**
     * Gets the bukkit entity.
     */
    override fun getBukkitEntity(): CraftWeepingAngel {
        if (this.entityBukkit == null) {
            entityBukkit = CraftWeepingAngel(this.world.server, this)

            val field = Entity::class.java.getDeclaredField("bukkitEntity")
            field.isAccessible = true
            field.set(this, entityBukkit)
        }

        return this.entityBukkit as CraftWeepingAngel
    }

    /**
     * Clears all entity aiGoals.
     */
    private fun clearAIGoals() {
        val dField = PathfinderGoalSelector::class.java.getDeclaredField("d")
        dField.isAccessible = true
        (dField.get(this.goalSelector) as MutableSet<*>).clear()
        (dField.get(this.targetSelector) as MutableSet<*>).clear()
    }
}