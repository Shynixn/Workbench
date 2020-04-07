package com.github.shynixn.workbench.bukkit.particle.dsl

import com.github.shynixn.workbench.minecraft.common.dsl.Position
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.stream.Stream

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
interface Circle {
    /**
     * Radius of the circle.
     */
    var radius: Double

    /**
     * X,Y,Z Axis rotation.
     */
    var rotation: Position

    /**
     * Density.
     */
    var density: Double

    /**
     * Adds a new particle to this circle.
     */
    fun particle(f: Particle.() -> Unit): Circle

    /**
     * Adds a new circle to this circle.
     */
    fun circle(f: Circle.() -> Unit): Circle

    /**
     * Adds a new delay to this circle.
     */
    fun delay(f: () -> Int): Circle

    /**
     * Skips the given angle.
     */
    fun skipAngle(f: () -> Double): Circle

    /**
     * Plays the particle.
     */
    fun play(location: Location, vararg players: Player)

    /**
     * Plays the particle.
     */
    fun play(location: Location, players: Collection<Player>)
}