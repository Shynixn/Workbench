package com.github.shynixn.workbench.bukkit.testsuite

import com.github.shynixn.workbench.bukkit.particle.dsl.circle
import com.github.shynixn.workbench.bukkit.particle.dsl.particle
import kotlinx.coroutines.delay
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
class ParticleTestSuite {
    suspend fun play(player: Player) {
        val location = player.location.add(2.0, 0.5, 0.0)


        circle {
            density = 360.0 * 2
            radius = 5.0
            circle {
                density = 360.0
                radius = 1.0
                particle {
                    name = "REDSTONE"
                    colorRed = 255
                    colorBlue = 0
                    colorGreen = 255
                }
            }
            particle {
                name = "REDSTONE"
                colorRed = 0
                colorBlue = 255
                colorGreen = 0
            }
            skipAngle {
                60.0
            }
        }.play(location, player)

       /* circle {
            density = 180.0
            radius = 5.0
            particle {
                name = "REDSTONE"
                colorRed = 0
                colorBlue = 255
                colorGreen = 0
            }
            delay {
                5
            }
            circle {
                density = 360.0
                radius = 1.0
                particle {
                    name = "REDSTONE"
                    colorRed = 0
                    colorBlue = 0
                    colorGreen = 255
                }
            }
            skipAngle {
                60.0
            }
            delay {
                5
            }
        }.play(location, player)*/


        /*circle {
            density = 180.0
            radius = 10.0
            particle {
                name = "REDSTONE"
                colorRed = 0
                colorBlue = 255
                colorGreen = 0
            }
            delay {
                5
            }
            particle {
                name = "REDSTONE"
                colorRed = 0
                colorBlue = 0
                colorGreen = 255
            }
            delay {
                5
            }
        }.play(location, player)*/


        /*   particle {
               name = "REDSTONE"
               colorRed = 0
               colorBlue = 255
               colorGreen = 0
           }.play(location, player)*/

        /*   circle {
                particle {
                    name = "REDSTONE"
                    colorRed = 0
                    colorBlue = 255
                    colorGreen = 0
                }
            }.play(location, player)*/


    }
}