package com.github.shynixn.workbench.minecraft.common.dsl

import com.github.shynixn.workbench.minecraft.common.implementation.PositionImpl
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

/**
 * Creates a new position.
 */
fun position(f: Position.() -> Unit): Position {
    val position = PositionImpl()
    f.invoke(position)
    return position
}


/**
 * Compares if this position and the other position are the same in the given properties.
 */
fun Position.matches(position: Position, vararg properties: KProperty<*>): Boolean {
    for (property in properties) {
        if (property == Position.x && this.x != position.x) {
            return false
        } else if (property == Position.y && this.y != position.y) {
            return false
        } else if (property == Position.z && this.z != position.z) {
            return false
        } else if (property == Position.yaw && this.yaw != position.yaw) {
            return false
        } else if (property == Position.pitch && this.pitch != position.pitch) {
            return false
        } else if (property == Position.worldName && this.worldName != position.worldName) {
            return false
        } else if (property == Position.blockX && this.blockX != position.blockX) {
            return false
        } else if (property == Position.blockY && this.blockY != position.blockY) {
            return false
        } else if (property == Position.blockZ && this.blockZ != position.blockZ) {
            return false
        }
    }

    return true
}

/**
 * Compares if this position and the other position are the same without the given properties.
 */
fun Position.matchesExcept(position: Position, vararg properties: KProperty<*>): Boolean {
    return matches(position, *Position.all.asSequence().filter { e -> !properties.contains(e) }.toList().toTypedArray())
}

