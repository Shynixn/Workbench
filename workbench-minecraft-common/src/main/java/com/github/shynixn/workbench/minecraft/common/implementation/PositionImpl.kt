package com.github.shynixn.workbench.minecraft.common.implementation

import com.github.shynixn.workbench.minecraft.common.dsl.Position
import com.github.shynixn.workbench.minecraft.common.dsl.Position.Companion.all
import javafx.geometry.Pos
import kotlin.math.sqrt
import kotlin.reflect.KProperty
import kotlin.streams.asStream
import kotlin.streams.toList

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
internal class PositionImpl : Position {
    /** [x] coordinate. */
    override var x: Double = 0.0
    /** [y] coordinate. */
    override var y: Double = 0.0
    /** [z] coordinate. */
    override var z: Double = 0.0
    /** [yaw] rotation yaw. */
    override var yaw: Double = 0.0
    /** [pitch] rotation pitch. */
    override var pitch: Double = 0.0
    /** [worldName] which world the location is. */
    override var worldName: String? = null

    /** [blockX] coordinate as Int. */
    override val blockX: Int
        get() = x.toInt()
    /** [blockY] coordinate as Int. */
    override val blockY: Int
        get() = y.toInt()
    /** [blockZ] coordinate as Int. */
    override val blockZ: Int
        get() = z.toInt()

    /**
     * Multiply position.
     */
    override fun multiply(multiplier: Double): Position {
        this.x *= multiplier
        this.y *= multiplier
        this.z *= multiplier

        return this
    }

    /**
     * Gets the distance to another position.
     */
    override fun distance(position: Position): Double {
        if (worldName != position.worldName) {
            return Double.MAX_VALUE
        }

        val distance = square(x - position.x) + square(y - position.y) + square(z - position.z)
        return sqrt(distance)
    }

    /**
     * Creates a copy of this object.
     */
    override fun clone(): Position {
        val current = this

        return PositionImpl().apply {
            x = current.x
            y = current.y
            z = current.z
            yaw = current.yaw
            pitch = current.pitch
            worldName = current.worldName
        }
    }

    /**
     * Adds the parameters to this position.
     */
    override fun add(position: Position): Position {
        return this.add(position.x, position.y, position.z, position.yaw, position.pitch)
    }

    /**
     * Adds the parameters to this position.
     */
    override fun add(x: Double, y: Double, z: Double, yaw: Double, pitch: Double): Position {
        this.x += x
        this.y += y
        this.z += z
        this.yaw += yaw
        this.pitch += pitch

        return this
    }

    /**
     * Returns the square number.
     */
    private fun square(number: Double): Double {
        return number * number
    }

    /**
     * Indicates whether some other object is "equal to" this one. Implementations must fulfil the following
     * requirements:
     *
     * * Reflexive: for any non-null value `x`, `x.equals(x)` should return true.
     * * Symmetric: for any non-null values `x` and `y`, `x.equals(y)` should return true if and only if `y.equals(x)` returns true.
     * * Transitive:  for any non-null values `x`, `y`, and `z`, if `x.equals(y)` returns true and `y.equals(z)` returns true, then `x.equals(z)` should return true.
     * * Consistent:  for any non-null values `x` and `y`, multiple invocations of `x.equals(y)` consistently return true or consistently return false, provided no information used in `equals` comparisons on the objects is modified.
     * * Never equal to null: for any non-null value `x`, `x.equals(null)` should return false.
     *
     * Read more about [equality](https://kotlinlang.org/docs/reference/equality.html) in Kotlin.
     */
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is Position) {
            return false
        }

        return this.x == other.x && this.y == other.y && this.z == other.z && this.yaw == other.yaw && this.pitch == other.pitch && this.worldName == other.worldName
    }

    /**
     * Calculates the hashCode.
     */
    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + yaw.hashCode()
        result = 31 * result + pitch.hashCode()
        result = 31 * result + (worldName?.hashCode() ?: 0)
        return result
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        val builder = StringBuilder()

        if (worldName != null) {
            builder.append("[$worldName] ")
        }

        builder.append("$x $y $z $yaw $pitch")

        return builder.toString()
    }
}