@file:Suppress("UNCHECKED_CAST")

package com.github.shynixn.workbench.bukkit.particle.implementation

import com.github.shynixn.workbench.bukkit.common.dsl.toPosition
import com.github.shynixn.workbench.bukkit.particle.dsl.Line
import org.bukkit.Location
import org.bukkit.entity.Player

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
class LineImpl : GroupImpl(), Line {
    /**
     * Density in percentage.
     */
    override var density: Double = 1.0

    /**
     * Skips the given distance.
     */
    override fun skipDistance(f: () -> Double): Line {
        val distance = f.invoke()
        actions.add(1 to distance)
        return this
    }

    /**
     * Plays the particle.
     */
    override fun play(begin: Location, end: Location, vararg players: Player) {
        play(begin, end, players.toList())
    }

    /**
     * Plays the particle.
     */
    override fun play(begin: Location, end: Location, players: Collection<Player>) {
        val calculatedSequence = sequence {
            val distance = begin.distance(end)
            val adder = end.subtract(begin).toVector().normalize().multiply(0.1)
            val normalizedVector = adder.clone()
            val amountOfActions = distance / adder.length()
            var i = 0

            while (i < amountOfActions) {
                val calculatedLocation = Location(
                    begin.world,
                    begin.x + normalizedVector.x,
                    begin.y + normalizedVector.y,
                    begin.z + normalizedVector.z
                )
                println(calculatedLocation.toPosition())
                yield(calculatedLocation)

                for (j in 0 until (1.0 / density).toInt()) {
                    normalizedVector.add(adder)
                    i++
                }
            }
        }

        val actionSequence = sequence {
            var index = 0
            val normalizedVector = end.subtract(begin).toVector().normalize().multiply(0.1)
            val vectorLength =  normalizedVector.length()

            while (true) {
                if (index >= actions.size) {
                    index = 0
                }

                val action = actions[index]

                if (action.first == 0) {
                    yield(action.second as ((Location, Collection<Player>) -> Unit))
                } else if (action.first == 1) {
                    val skipDistance = action.second as Double
                    val skipActions = (skipDistance / vectorLength).toInt()
                    val emptyAction = { _: Location, _: Collection<Player> -> Unit }

                    for (i in 0 until skipActions) {
                        yield(emptyAction)
                    }
                }

                index++
            }
        }

        calculatedSequence.zip(actionSequence).forEach { zipItem ->
            zipItem.second.invoke(zipItem.first, players)
        }
    }
}