@file:Suppress("UNCHECKED_CAST")

package com.github.shynixn.workbench.bukkit.particle.implementation

import com.github.shynixn.workbench.bukkit.async.dsl.async
import com.github.shynixn.workbench.bukkit.async.dsl.launchAsync
import com.github.shynixn.workbench.bukkit.particle.dsl.Circle
import com.github.shynixn.workbench.bukkit.particle.dsl.Group
import com.github.shynixn.workbench.bukkit.particle.dsl.Line
import com.github.shynixn.workbench.bukkit.particle.dsl.Particle
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
abstract class GroupImpl : Group {
    /**
     * Ordered list of actions.
     */
    val actions: MutableList<Pair<Int, Any>> = ArrayList()

    /**
     * Adds a new particle to this group.
     */
    override fun particle(amount: Int, f: Particle.() -> Unit) {
        val particle = ParticleImpl()
        f.invoke(particle)
        for (i in 0 until amount) {
            actions.add(0 to { location: Location, players: Collection<Player> ->
                particle.play(location, players)
            })
        }
    }

    /**
     * Adds a line to this group.
     */
    override fun line(amount: Int, f: Line.() -> Unit) {
        val line = LineImpl()
        f.invoke(line)
        for (i in 0 until amount) {
            actions.add(0 to { location: Location, players: Collection<Player> ->
                line.play(location, players)
            })
        }
    }

    /**
     * Adds a new circle to this group.
     */
    override fun circle(amount: Int, f: Circle.() -> Unit) {
        val circle = CircleImpl()
        f.invoke(circle)
        for (i in 0 until amount) {
            actions.add(0 to { location: Location, players: Collection<Player> ->
                circle.play(location, players)
            })
        }
    }

    /**
     * Adds a new delay to this group.
     */
    override fun delay(amount: Int, f: () -> Int) {
        val delayAmount = f.invoke().toLong()
        for (i in 0 until amount) {
            actions.add(0 to { _: Location, _: Collection<Player> ->
                launchAsync {
                    kotlinx.coroutines.delay(delayAmount)
                }
            })
        }
    }
}