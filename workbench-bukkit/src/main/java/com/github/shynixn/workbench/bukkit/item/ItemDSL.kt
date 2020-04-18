package com.github.shynixn.workbench.bukkit.item

import com.github.shynixn.workbench.bukkit.common.findClazz
import com.github.shynixn.workbench.bukkit.common.log
import com.github.shynixn.workbench.bukkit.common.translateChatColors
import com.github.shynixn.workbench.bukkit.common.workbenchResource
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
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
 * Tries to convert the given object to a material.
 */
fun Any.toMaterial(): Material {
    return findMaterialFromDescriptor(this)
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
 * Converts the object to an item.
 */
fun ItemStack.toItem(): Item {
    val item = item {}
    item.name = this.type.name
    item.amount = this.amount
    item.dataValue = this.durability.toInt()

    if (this.itemMeta != null) {
        item.displayName = this.itemMeta!!.displayName
        item.lore = this.itemMeta!!.lore
    }

    item.skin = if (this.itemMeta != null && this.itemMeta is SkullMeta) {
        val currentMeta = this.itemMeta as SkullMeta
        val owner = currentMeta.owner

        if (!owner.isNullOrEmpty()) {
            owner
        } else {
            val cls =
                findClazz("org.bukkit.craftbukkit.VERSION.inventory.CraftMetaSkull")
            val real = cls.cast(currentMeta)
            val field = real.javaClass.getDeclaredField("profile")
            field.isAccessible = true
            val profile = field.get(real) as GameProfile?

            if (profile == null) {
                null
            } else {
                profile.properties.get("textures").toTypedArray()[0].value
            }
        }
    } else {
        null
    }

    val nmsItemStackClass =
        findClazz("net.minecraft.server.VERSION.ItemStack")
    val craftItemStackClass =
        findClazz("org.bukkit.craftbukkit.VERSION.inventory.CraftItemStack")
    val nmsCopyMethod = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack::class.java)
    val getNBTTag = nmsItemStackClass.getDeclaredMethod("getTag")
    val nmsItemStack = nmsCopyMethod.invoke(null, this)
    val targetNbtTag = getNBTTag.invoke(nmsItemStack)

    if (targetNbtTag != null) {
        item.nbtTag = targetNbtTag.toString()
    }

    return item
}

/**
 * Converts the object to an itemStack.
 * Throws an exception if not correctly specified.
 */
@Suppress("UNCHECKED_CAST")
fun Item.toItemStack(): ItemStack {
    val material = findMaterialFromDescriptor(this.name)
    val itemStack = ItemStack(material, this.amount, this.dataValue.toShort())

    if (itemStack.itemMeta != null) {
        var currentMeta = itemStack.itemMeta

        if (!skin.isNullOrEmpty() && currentMeta is SkullMeta) {
            var newSkin = skin!!

            if (newSkin.length > 32) {
                val cls =
                    findClazz("org.bukkit.craftbukkit.VERSION.inventory.CraftMetaSkull")
                val real = cls.cast(currentMeta)
                val field = real.javaClass.getDeclaredField("profile")
                val newSkinProfile = GameProfile(UUID.randomUUID(), null)

                if (newSkin.contains("textures.minecraft.net")) {
                    if (!newSkin.startsWith("http://")) {
                        newSkin = "http://$newSkin"
                    }

                    newSkin = Base64Coder.encodeString("{textures:{SKIN:{url:\"$newSkin\"}}}")
                }

                newSkinProfile.properties.put("textures", Property("textures", newSkin))
                field.isAccessible = true
                field.set(real, newSkinProfile)
                currentMeta = SkullMeta::class.java.cast(real)
            } else {
                currentMeta.owner = newSkin
            }
        }

        if (displayName != null) {
            currentMeta!!.setDisplayName(displayName!!.translateChatColors())
        }

        if (lore != null) {
            currentMeta!!.lore = lore!!.map { l -> l.translateChatColors() }
        }

        itemStack.itemMeta = currentMeta
    }

    if (nbtTag == null) {
        return itemStack
    }

    val nmsItemStackClass =
        findClazz("net.minecraft.server.VERSION.ItemStack")
    val craftItemStackClass =
        findClazz("org.bukkit.craftbukkit.VERSION.inventory.CraftItemStack")
    val nmsCopyMethod = craftItemStackClass.getDeclaredMethod("asNMSCopy", ItemStack::class.java)
    val nmsToBukkitMethod = craftItemStackClass.getDeclaredMethod("asBukkitCopy", nmsItemStackClass)
    val nbtTagClass =
        findClazz("net.minecraft.server.VERSION.NBTTagCompound")
    val getNBTTag = nmsItemStackClass.getDeclaredMethod("getTag")
    val setNBTTag = nmsItemStackClass.getDeclaredMethod("setTag", nbtTagClass)

    val nmsItemStack = nmsCopyMethod.invoke(null, itemStack)
    var targetNbtTag = getNBTTag.invoke(nmsItemStack)

    if (targetNbtTag == null) {
        targetNbtTag = nbtTagClass.newInstance()
    }

    val compoundMapField = nbtTagClass.getDeclaredField("map")
    compoundMapField.isAccessible = true
    val targetNbtMap = compoundMapField.get(targetNbtTag) as MutableMap<Any?, Any?>

    try {
        val sourceNbtTag = findClazz("net.minecraft.server.VERSION.MojangsonParser")
            .getDeclaredMethod("parse", String::class.java).invoke(null, nbtTag)
        val sourceNbtMap = compoundMapField.get(sourceNbtTag) as MutableMap<Any?, Any?>

        for (key in sourceNbtMap.keys) {
            targetNbtMap[key] = sourceNbtMap[key]
        }

        setNBTTag.invoke(nmsItemStack, targetNbtTag)
    } catch (e: Exception) {
        log {
            error { "Cannot parse NBT '$nbtTag'" }
            throwable { e }
        }
    }

    return nmsToBukkitMethod.invoke(null, nmsItemStack) as ItemStack
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