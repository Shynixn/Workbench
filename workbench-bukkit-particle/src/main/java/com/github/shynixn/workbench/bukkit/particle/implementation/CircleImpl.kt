@file:Suppress("UNCHECKED_CAST")

package com.github.shynixn.workbench.bukkit.particle.implementation

import com.github.shynixn.workbench.bukkit.async.dsl.async
import com.github.shynixn.workbench.bukkit.particle.dsl.Circle
import com.github.shynixn.workbench.bukkit.particle.dsl.Particle
import com.github.shynixn.workbench.minecraft.common.dsl.Position
import com.github.shynixn.workbench.minecraft.common.dsl.position
import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.stream.Stream
import kotlin.math.cos
import kotlin.math.sin

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
class CircleImpl : Circle {
    /**
     * Ordered list of actions.
     */
    private val actions: MutableList<Pair<Int, Any>> = ArrayList()

    /**
     * Radius of the circle.
     */
    override var radius: Double = 1.0
    /**
     * X,Y,Z Axis rotation.
     */
    override var rotation: Position = position {
        x = 0.0
        y = 1.0
        z = 0.0
    }
    /**
     *  Density.
     */
    override var density: Double = 360.0

    /**
     * Adds a new particle to this circle.
     */
    override fun particle(f: Particle.() -> Unit): Circle {
        val particle = ParticleImpl()
        f.invoke(particle)
        actions.add(0 to { location: Location, players: Collection<Player> ->
            particle.play(location, players)
        })
        return this
    }

    /**
     * Adds a new circle to this circle.
     */
    override fun circle(f: Circle.() -> Unit): Circle {
        val circle = CircleImpl()
        f.invoke(circle)
        actions.add(0 to { location: Location, players: Collection<Player> ->
            circle.play(location, players)
        })
        return this
    }

    /**
     * Adds a new delay to this circle.
     */
    override fun delay(f: () -> Int): Circle {
        val delayAmount = f.invoke().toLong()
        actions.add(0 to { _: Location, _: Collection<Player> ->
            async {
                delay(delayAmount)
            }
        })
        return this
    }

    /**
     * Skips the given angle.
     */
    override fun skipAngle(f: () -> Double): Circle {
        val angle = f.invoke()
        actions.add(1 to angle)
        return this
    }

    /**
     * Plays the particle.
     */
    override fun play(location: Location, vararg players: Player) {
        play(location, players.toList())
    }

    /**
     * Plays the particle.
     */
    override fun play(location: Location, players: Collection<Player>) = async {
        val calculatedSequence = sequence {
            val adder = 360.0 / density
            var sum = 0.0
            val radius = radius

            while (sum < 360) {
                val radian = Math.toRadians(sum)
                val x = radius * cos(radian)
                val y = radius * sin(radian)
                val calculatedLocation = Location(location.world, location.x + x, location.y, location.z + y)
                yield(calculatedLocation)

                sum += adder
            }
        }

        val actionSequence = sequence {
            var index = 0
            val adder = 360.0 / density
            while (true) {
                if (index >= actions.size) {
                    index = 0
                }

                val action = actions[index]

                if (action.first == 0) {
                    yield(action.second as ((Location, Collection<Player>) -> Unit))
                } else if (action.first == 1) {
                    val skipAngle = action.second as Double
                    val skipActions = (skipAngle / adder).toInt()
                    val emptyAction = { _: Location, _: Collection<Player> -> Unit }

                    for (i in 0 until skipActions) {
                        yield(emptyAction)
                    }
                }

                index++
            }
        }

        calculatedSequence.zip(actionSequence).forEach { zipItem ->
            val nextAngle = zipItem.second.invoke(zipItem.first, players)
        }
    }
}