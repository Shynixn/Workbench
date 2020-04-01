package com.github.shynixn.workbench.bukkit.item.dsl

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
interface Item {
    companion object {
        val name: KProperty<*> = Item::name
        val amount: KProperty<*> = Item::amount
        val dataValue: KProperty<*> = Item::dataValue
        val displayName: KProperty<*> = Item::displayName
        val lore: KProperty<*> = Item::lore
        val skin: KProperty<*> = Item::skin
        val nbtTag: KProperty<*> = Item::nbtTag
        val all: List<KProperty<*>> = arrayListOf(name, amount, dataValue, displayName, lore, skin, nbtTag)
    }

    /**
     * Name of the item.
     */
    var name: Any

    /**
     * Amount of the item.
     */
    var amount: Int

    /**
     * Data value of the item.
     */
    var dataValue: Int

    /**
     * DisplayName.
     */
    var displayName: String?

    /**
     * Lore of the item.
     */
    var lore: List<String>?

    /**
     * Skin of the item.
     */
    var skin: String?

    /**
     * Nbt tag description.
     */
    var nbtTag: String?
}