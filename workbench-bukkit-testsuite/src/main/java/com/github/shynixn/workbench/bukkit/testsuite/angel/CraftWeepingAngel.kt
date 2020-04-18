package com.github.shynixn.workbench.bukkit.testsuite.angel

import net.minecraft.server.v1_14_R1.EntityInsentient
import org.bukkit.craftbukkit.v1_14_R1.CraftServer
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftLivingEntity
import org.bukkit.entity.*
import org.bukkit.loot.LootTable

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
class CraftWeepingAngel(server: CraftServer, nmsPet: EntityInsentient) : CraftLivingEntity(server, nmsPet), Zombie,
    EntityProxy {
    /**
     * Removes this entity.
     */
    override fun deleteFromWorld() {
        super.remove()
    }

    /**
     * Hides the true type of the entity.
     */
    override fun getType(): org.bukkit.entity.EntityType {
        return org.bukkit.entity.EntityType.ZOMBIE
    }

    override fun isBaby(): Boolean {
        return false
    }

    override fun setVillager(p0: Boolean) {
    }

    /**
     * Ignore all other plugins trying to remove this entity. This is the entity of this plugin
     * no one else is allowed to modify this!
     */
    override fun remove() {
    }

    override fun isPersistent(): Boolean {
        return false
    }

    override fun isConverting(): Boolean {
        return false
    }

    override fun isVillager(): Boolean {
        return false
    }

    /**
     * Pet should never be persistent.
     */
    override fun setPersistent(b: Boolean) {}

    /**
     * Custom type.
     */
    override fun toString(): String {
        return "WeepingAngel{Entity}"
    }

    override fun setVillagerProfession(p0: Villager.Profession?) {
    }

    override fun getLootTable(): LootTable? {
        return null
    }

    override fun setBaby(p0: Boolean) {
    }

    override fun getVillagerProfession(): Villager.Profession? {
        return null
    }

    override fun setTarget(p0: LivingEntity?) {
    }

    override fun getTarget(): LivingEntity? {
        return null
    }

    override fun setConversionTime(p0: Int) {
    }

    override fun setLootTable(p0: LootTable?) {
    }

    override fun setSeed(p0: Long) {
    }

    override fun getConversionTime(): Int {
        return 0
    }

    override fun getSeed(): Long {
        return 0L
    }
}