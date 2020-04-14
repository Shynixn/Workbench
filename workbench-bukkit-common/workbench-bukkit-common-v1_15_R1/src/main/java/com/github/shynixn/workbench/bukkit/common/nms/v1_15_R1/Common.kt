package com.github.shynixn.workbench.bukkit.common.nms.v1_15_R1

import com.github.shynixn.workbench.minecraft.common.dsl.CommonNMS
import net.minecraft.server.v1_15_R1.EntityLiving
import net.minecraft.server.v1_15_R1.GenericAttributes
import net.minecraft.server.v1_15_R1.IAttribute
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity

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
class Common : CommonNMS {
    /**
     * Gets the nms entity from the bukkit entity.
     */
    override fun getNMSEntityFromBukkitEntity(entity: Any): Any {
        return (entity as CraftEntity).handle
    }

    /**
     * Applies the given generic attribute.
     */
    override fun applyGenericAttribute(entity: Any, name: String, value: Double) {
        val nmsEntity = getNMSEntityFromBukkitEntity(entity) as EntityLiving

        val genericAttribute = GenericAttributes::class.java.declaredFields.asSequence()
            .filter { e -> e.type == IAttribute::class.java }.map { e -> e.get(null) as IAttribute }.first { e ->
                e.name == name
            }

        nmsEntity.getAttributeInstance(genericAttribute).value = value
    }
}