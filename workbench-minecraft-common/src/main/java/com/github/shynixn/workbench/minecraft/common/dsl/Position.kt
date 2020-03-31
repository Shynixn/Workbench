package com.github.shynixn.workbench.minecraft.common.dsl

/**
 * Created by Shynixn 2019.
 * <p>
 * Version 1.2
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2019 by Shynixn
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

interface Position {
    /** [worldName] which world the location is. */
    var worldName: String?

    /** [x] coordinate. */
    var x: Double

    /** [y] coordinate. */
    var y: Double

    /** [z] coordinate. */
    var z: Double

    /** [yaw] rotation yaw. */
    var yaw: Double

    /** [pitch] rotation pitch. */
    var pitch: Double

    /** [blockX] coordinate as Int. */
    val blockX: Int

    /** [blockY] coordinate as Int. */
    val blockY: Int

    /** [blockZ] coordinate as Int. */
    val blockZ: Int

    /**
     * Adds the given [position] from this position
     * and returns this position.
     */
    fun add(position: Position): Position

    /**
     * Adds the parameters to this position.
     */
    fun add(x: Double, y: Double = 0.0, z: Double = 0.0, yaw: Double = 0.0, pitch: Double = 0.0): Position

    /**
     * Multiply position.
     */
    fun multiply(multiplier: Double): Position

    /**
     * Gets the distance to another position.
     */
    fun distance(position: Position): Double

    /**
     * Creates a deep copy of this object.
     */
    fun clone(): Position
}