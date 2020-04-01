package com.github.shynixn.workbench.bukkit.item.dsl

import com.github.shynixn.workbench.bukkit.common.dsl.workbenchResource
import com.github.shynixn.workbench.bukkit.item.implementation.ItemImpl
import com.github.shynixn.workbench.bukkit.item.implementation.ItemWorkbenchResource
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
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


private var resource =
    ItemWorkbenchResource()

/**
 * Registers the resources if not already registered.
 */
private fun registerIfNotRegistered() {
    if (!resource.registered) {
        workbenchResource.registerSubResource(resource)
        resource.registered = true
    }
}

/**
 * Creates a new item object.
 */
fun item(f: Item.() -> Unit): Item {
    val item = ItemImpl()
    f.invoke(item)
    return item
}

/**
 * Converts the object to an itemStack.
 * Throws an exception if not correctly specified.
 */
fun Item.toItemStack(): ItemStack {
    val material = findMaterialFromDescriptor(this.name)
    throw NotImplementedError()
}

/**
 * Finds a material from a descriptor.
 */
private fun findMaterialFromDescriptor(descriptor: Any): Material {
    if (resource.materialCache.containsKey(descriptor)) {
        return resource.materialCache[descriptor]!!
    }

    // Check if the descriptor is a material.
    if (descriptor is Material) {
        resource.materialCache[descriptor] = descriptor
        return resource.materialCache[descriptor]!!
    }

    // Check if the descriptor is an integer.
    val intHint: Int? = if (descriptor is Int) {
        descriptor
    } else if (descriptor is String && descriptor.toIntOrNull() != null) {
        descriptor.toInt()
    } else {
        null
    }

    if (intHint != null) {
        // It is a number.
        val idField = Material::class.java.getDeclaredField("id")
        idField.isAccessible = true

        for (material in Material::class.java.enumConstants) {
            if (idField.get(material) as Int == intHint) {
                resource.materialCache[descriptor] = material
                return resource.materialCache[descriptor]!!
            }
        }
    }

    // Check if the descriptor is a string.
    if (descriptor is String) {
        for (material in Material::class.java.enumConstants) {
            try {
                if (material.name.equals(descriptor, true) || ("LEGACY_$descriptor" == material.name)) {
                    resource.materialCache[descriptor] = material
                    return resource.materialCache[descriptor]!!
                }
            } catch (e: Exception) {
            }
        }
    }

    throw IllegalArgumentException("Item {$descriptor} cannot be transformed!")
}

/**
 * Compares if this item and the other item are the same in the given properties.
 */
fun Item.matches(item: Item, vararg properties: KProperty<*>): Boolean {
    for (property in properties) {
        if (property == Item.name && findMaterialFromDescriptor(this.name) != findMaterialFromDescriptor(item.name)) {
            return false
        } else if (property == Item.amount && this.amount == item.amount) {
            return false
        } else if (property == Item.dataValue && this.dataValue == item.dataValue) {
            return false
        } else if (property == Item.displayName && this.displayName == item.displayName) {
            return false
        } else if (property == Item.lore && this.lore == item.lore) {
            return false
        } else if (property == Item.skin && this.skin == item.skin) {
            return false
        } else if (property == Item.nbtTag && this.nbtTag == item.nbtTag) {
            return false
        }
    }

    return true
}

/**
 * Compares if this item and the other item are the same without the given properties.
 */
fun Item.matchesExcept(item: Item, vararg properties: KProperty<*>): Boolean {
    return matches(item, *Item.all.asSequence().filter { e -> !properties.contains(e) }.toList().toTypedArray())
}