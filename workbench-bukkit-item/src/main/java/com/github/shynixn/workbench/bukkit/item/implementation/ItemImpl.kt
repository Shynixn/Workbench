package com.github.shynixn.workbench.bukkit.item.implementation

import com.github.shynixn.workbench.bukkit.item.dsl.Item
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
internal class ItemImpl : Item {
    /**
     * Name of the item.
     */
    override var name: Any = "0"
    /**
     * Amount of the item.
     */
    override var amount: Int = 1
    /**
     * Data value of the item.
     */
    override var dataValue: Int = 0
    /**
     * DisplayName.
     */
    override var displayName: String? = null
    /**
     * Lore of the item.
     */
    override var lore: List<String>? = null
    /**
     * Skin of the item.
     */
    override var skin: String? = null
    /**
     * Nbt tag description.
     */
    override var nbtTag: String? = null

    /**
     * Equals implementation.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (name != other.name) return false
        if (amount != other.amount) return false
        if (dataValue != other.dataValue) return false
        if (displayName != other.displayName) return false
        if (lore != other.lore) return false
        if (skin != other.skin) return false
        if (nbtTag != other.nbtTag) return false

        return true
    }

    /**
     * Hashcode implementation.
     */
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + amount
        result = 31 * result + dataValue
        result = 31 * result + (displayName?.hashCode() ?: 0)
        result = 31 * result + (lore?.hashCode() ?: 0)
        result = 31 * result + (skin?.hashCode() ?: 0)
        result = 31 * result + (nbtTag?.hashCode() ?: 0)
        return result
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        val builder = StringBuilder()

        builder.append("Item {Name:$name,Amount:$amount,Data:$dataValue")

        if (displayName != null) {
            builder.append(",Display:$displayName")
        }

        if (lore != null) {
            builder.append(",Lore:${lore!!.joinToString(",")}")
        }

        if (skin != null) {
            builder.append(",Skin:$skin")
        }

        if (nbtTag != null) {
            builder.append(",NBT:$nbtTag")
        }

        builder.append("}")

        return builder.toString()
    }
}